package com.example.zd_x.faceverification.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.mvp.p.compl.HomePresenterCompl;
import com.example.zd_x.faceverification.mvp.view.IHomeView;
import com.example.zd_x.faceverification.ui.adapter.HistoryVerificationListViewAdapter;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtil;
import com.hanvon.face.HWCoreHelper;
import com.hanvon.faceRec.Consts;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements IHomeView {
    private static final String TAG = "HomeActivity";

    private static final String[] PERMISSION = new String[]{Manifest.permission.VIBRATE, Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @BindView(R.id.bt_frontCamera_home)
    Button btFrontCameraHome;
    @BindView(R.id.bt_rearCamera_home)
    Button btRearCameraHome;
    @BindView(R.id.bt_usbCamera_home)
    Button btUsbCameraHome;
    @BindView(R.id.rv_showImageMsg_home)
    RecyclerView rvShowImageMsgHome;
    private HomePresenterCompl homePresenterCompl;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void OnActCreate(Bundle savedInstanceState) {
        homePresenterCompl = new HomePresenterCompl(this);
    }

    @Override
    protected void initEvent() {
        permissions();
    }

    private void permissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION, ConstsUtils.CODE_FOR_WRITE_PERMISSION);
            Log.e(TAG, "permissions: 2222" );
        } else {
            Log.e(TAG, "permissions: 1111" );
            homePresenterCompl.findHistoryResult();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == ConstsUtils.CODE_FOR_WRITE_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                //用户同意，执行操作
                homePresenterCompl.findHistoryResult();
            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                }
            }
        }
    }

    @OnClick({R.id.bt_frontCamera_home, R.id.bt_rearCamera_home, R.id.bt_usbCamera_home})
    public void onClick(Button view) {
        switch (view.getId()) {
            case R.id.bt_frontCamera_home:
                LogUtil.d("bt_frontCamera_home");
                startCamera(ConstsUtils.FRONT_CAMERA);
                break;
            case R.id.bt_rearCamera_home:
                LogUtil.d("bt_rearCamera_home");
                startCamera(ConstsUtils.REAR_CAMERA);
                break;
            case R.id.bt_usbCamera_home:
                LogUtil.d("bt_usbCamera_home");
//                startCamera(ConstsUtils.USB_CAMERA);
                break;
        }
    }

    private void startCamera(int cameraId) {
        Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
        intent.putExtra("cameraId", cameraId + "");

        startActivity(intent);
    }



    @Override
    public void getHistoryMsg(List<HistoryVerificationResultModel> list) {
        if (list.size()==0){
            return;
        }
        HistoryVerificationListViewAdapter historyAdapter = new HistoryVerificationListViewAdapter(this, list);
        //必须先设置LayoutManager
        rvShowImageMsgHome.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this,R.drawable.recyclerview_divider_style));
        rvShowImageMsgHome.addItemDecoration(divider);
        rvShowImageMsgHome.setAdapter(historyAdapter);
    }
}
