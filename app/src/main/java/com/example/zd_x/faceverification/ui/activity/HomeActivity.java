package com.example.zd_x.faceverification.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Button;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.callBack.LoadCallBack;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtils;
import com.example.zd_x.faceverification.utils.OkHttpManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class HomeActivity extends BaseActivity {
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


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void OnActCreate(Bundle savedInstanceState) {

    }

    @Override
    protected void initEvent() {
        permissions();
    }

    private void permissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, PERMISSION, 1000);
        }else {

        }
    }

    @OnClick({R.id.bt_frontCamera_home, R.id.bt_rearCamera_home, R.id.bt_usbCamera_home})
    public void onClick(Button view) {
        switch (view.getId()) {
            case R.id.bt_frontCamera_home:
                LogUtils.d(TAG, "bt_frontCamera_home");
                startCamera(ConstsUtils.FRONT_CAMERA);
                break;
            case R.id.bt_rearCamera_home:
                LogUtils.d(TAG, "bt_rearCamera_home");
                startCamera(ConstsUtils.REAR_CAMERA);
                break;
            case R.id.bt_usbCamera_home:
                LogUtils.d(TAG, "bt_usbCamera_home");

//                startCamera(ConstsUtils.USB_CAMERA);

                break;
        }
    }

    private void startCamera(int cameraId) {

        Intent intent = new Intent(HomeActivity.this, CameraActivity.class);
        intent.putExtra("cameraId", cameraId+"");
        startActivity(intent);
    }
}
