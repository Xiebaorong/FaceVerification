package com.example.zd_x.faceverification.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.mvp.p.compl.HomePresenterCompl;
import com.example.zd_x.faceverification.mvp.view.IHomeView;
import com.example.zd_x.faceverification.ui.adapter.HistoryVerificationListViewAdapter;
import com.example.zd_x.faceverification.ui.widget.PagingScrollHelper;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements IHomeView, PagingScrollHelper.onPageChangeListener {
    private static final String TAG = "HomeActivity";

    private static final String[] PERMISSION = new String[]{Manifest.permission.VIBRATE, Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @BindView(R.id.fl_showHistoryMsg_home)
    FrameLayout flShowHistoryMsgHome;
    @BindView(R.id.iv_openCamera_home)
    ImageView ivOpenCameraHome;
    @BindView(R.id.rv_showImageMsg_home)
    RecyclerView rvShowImageMsgHome;
    private HomePresenterCompl homePresenterCompl;
    private HistoryVerificationListViewAdapter historyAdapter;
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
        initView();
        permissions();
    }

    private void initView() {
        PagingScrollHelper scrollHelper = new PagingScrollHelper();
        scrollHelper.setUpRecycleView(rvShowImageMsgHome);
        scrollHelper.setOnPageChangeListener(this);
        //必须先设置LayoutManager
        rvShowImageMsgHome.setLayoutManager(new LinearLayoutManager(this));
        //添加自定义分割线
        rvShowImageMsgHome.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        historyAdapter = new HistoryVerificationListViewAdapter(this, new ArrayList<HistoryVerificationResultModel>());
        rvShowImageMsgHome.setAdapter(historyAdapter);
    }


    private void permissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION, ConstsUtils.CODE_FOR_WRITE_PERMISSION);
            Log.e(TAG, "permissions: 权限未申请");
        } else {
            Log.e(TAG, "permissions: 权限以申请");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == ConstsUtils.CODE_FOR_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivOpenCameraHome.setVisibility(View.VISIBLE);
        homePresenterCompl.findHistoryResult(0);

    }

    @OnClick(R.id.iv_openCamera_home)
    public void onClick(ImageView view) {
        switch (view.getId()) {
            case R.id.iv_openCamera_home:
                LogUtil.d("bt_frontCamera_home");
                ivOpenCameraHome.setVisibility(View.GONE);
                startCamera(ConstsUtils.REAR_CAMERA);
                break;
            default:
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
        if (list.size() == 0) {
            return;
        }
        historyAdapter.addData(list,0, list.size());
    }

    boolean isPullUp;
    int index;
    @Override
    public void onPageChange(int index, boolean flag) {
        isPullUp = flag;
        if (flag) {
            homePresenterCompl.findHistoryResult(index);
            this.index = index;
        }
    }
}
