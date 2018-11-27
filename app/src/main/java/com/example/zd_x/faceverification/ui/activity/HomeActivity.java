package com.example.zd_x.faceverification.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.http.UpdateAppHttpUtil;
import com.example.zd_x.faceverification.mvp.model.HistoryVerificationResultModel;
import com.example.zd_x.faceverification.mvp.p.compl.HomePresenterCompl;
import com.example.zd_x.faceverification.mvp.view.IHomeView;
import com.example.zd_x.faceverification.ui.adapter.HistoryVerificationListViewAdapter;
import com.example.zd_x.faceverification.ui.service.UpDateService;
import com.example.zd_x.faceverification.ui.widget.PagingScrollHelper;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtil;
import com.example.zd_x.faceverification.utils.SharedPreferencesUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements IHomeView, PagingScrollHelper.onPageChangeListener {
    private static final String TAG = "HomeActivity";
    private static final String[] PERMISSION = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    @BindView(R.id.fl_showHistoryMsg_home)
    FrameLayout flShowHistoryMsgHome;
    @BindView(R.id.iv_openCamera_home)
    ImageView ivOpenCameraHome;
    @BindView(R.id.srl_refresh_home)
    SmartRefreshLayout srlRefreshHome;
    @BindView(R.id.rv_showImageMsg_home)
    RecyclerView rvShowImageMsgHome;
    private HomePresenterCompl homePresenterCompl;
    private List<HistoryVerificationResultModel> historylist = new ArrayList<>();
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
        startService(new Intent(this, UpDateService.class));
    }

    @Override
    protected void initEvent() {
        initView();
        permissions();
    }

    @Override
    protected void onNetChanged(int netWorkState) {

    }

    private void initView() {
        //必须先设置LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvShowImageMsgHome.setLayoutManager(linearLayoutManager);
        //添加自定义分割线
        rvShowImageMsgHome.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        historyAdapter = new HistoryVerificationListViewAdapter(this, historylist);
        rvShowImageMsgHome.setAdapter(historyAdapter);
        PagingScrollHelper scrollHelper = new PagingScrollHelper();
        scrollHelper.setUpRecycleView(rvShowImageMsgHome);
        scrollHelper.setOnPageChangeListener(this);
        srlRefreshHome.setEnableRefresh(false);//是否启用下拉刷新功能
        srlRefreshHome.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
        srlRefreshHome.setEnableLoadMoreWhenContentNotFull(false);
    }


    private void permissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSION, ConstsUtils.CODE_FOR_WRITE_PERMISSION);
            LogUtil.e("permissions: 权限未申请");
            SharedPreferencesUtils.getInstance().savePermissions(getString(R.string.cameraPermissions), false);
            SharedPreferencesUtils.getInstance().savePermissions(getString(R.string.storePermissions), false);
            return;
        } else {
            SharedPreferencesUtils.getInstance().savePermissions(getString(R.string.cameraPermissions), true);
            SharedPreferencesUtils.getInstance().savePermissions(getString(R.string.storePermissions), true);
            if (networkStatus >= 0) {
                Log.e(TAG, "permissions: -----------");
                homePresenterCompl.findHistoryResult(0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ConstsUtils.CODE_FOR_WRITE_PERMISSION) {
            Log.e(TAG, "onRequestPermissionsResult:00000 ");
            for (int i = 0; i < grantResults.length; i++) {
                int result = grantResults[i];
                String permission = permissions[i];
                if (result == 0) {
                    SharedPreferencesUtils.getInstance().savePermissions(permission, true);
                } else {
                    SharedPreferencesUtils.getInstance().savePermissions(permission, false);
                }
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.e(TAG, "onRequestPermissionsResult: 222222");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivOpenCameraHome.setVisibility(View.VISIBLE);
        if (networkStatus >= 0) {
            homePresenterCompl.findHistoryResult(0);

        }
    }

    @OnClick(R.id.iv_openCamera_home)
    public void onClick(ImageView view) {
        switch (view.getId()) {
            case R.id.iv_openCamera_home:
                if (SharedPreferencesUtils.getInstance().getPermission(getString(R.string.cameraPermissions))) {
                    startCamera(ConstsUtils.REAR_CAMERA);
                } else {
                    showToast("相机权限未开启");
                }
                break;
            default:
                break;

        }
    }

    private void startCamera(int cameraId) {
        ivOpenCameraHome.setVisibility(View.GONE);
        Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
        intent.putExtra("cameraId", cameraId + "");
        startActivity(intent);
    }

    @Override
    public void onPageChange(int index, boolean flag) {
        if (flag) {
            homePresenterCompl.findHistoryResult(index);
            srlRefreshHome.finishRefresh();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public List<HistoryVerificationResultModel> getHistoryMsg() {
        return historylist;
    }

    @Override
    public void notifyDataSetChanged() {
        historyAdapter.notifyDataSetChanged();
    }

    @Override
    public void partItemnotifyDataSetChanged() {
        historyAdapter.partItemChanged(historylist);
    }

    @Override
    public void appUpdate() {
        /**
         * 静默下载，下载完才弹出升级界面
         */
        new UpdateAppManager
                .Builder()
                //当前Activity
                .setActivity(this)
                //更新地址
                .setUpdateUrl("")
                //实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                //只有wifi下进行，静默下载(只对静默下载有效)
                .setOnlyWifi()
                .build()
                .silenceUpdate();
    }

}
