package com.example.zd_x.faceverification.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.mvp.p.compl.CameraPresenterCompl;
import com.example.zd_x.faceverification.mvp.view.ICameraView;
import com.example.zd_x.faceverification.ui.widget.AutoFitTextureView;
import com.example.zd_x.faceverification.ui.widget.FaceFrameTextureView;
import com.example.zd_x.faceverification.utils.ButtonUtils;
import com.example.zd_x.faceverification.utils.CameraHelper;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtil;
import com.hanvon.faceRec.Camera2Helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class Camera2Activity extends BaseActivity implements ICameraView {
    private static final String TAG = "Camera2Activity";
    @BindView(R.id.at_texture_camera2Fragment)
    AutoFitTextureView mTextureView;
    @BindView(R.id.tv_faceShow_camera2Fragment)
    FaceFrameTextureView tvFaceShowCamera2Fragment;
    @BindView(R.id.iv_photograph_camera2Fragment)
    ImageView ivPhotographCamera;
    @BindView(R.id.iv_switchoverCamera_camera2Fragment)
    ImageView ivSwitchoverCameraCamera;
    @BindView(R.id.iv_back_camera2Fragment)
    ImageView ivBackCamera;
    private CameraPresenterCompl iCameraPresenter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstsUtils.WHAT_SHOW_MSG:
//                    tvHintTextCamera.setText((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_camera2;
    }

    @Override
    protected void OnActCreate(Bundle savedInstanceState) {
        iCameraPresenter = new CameraPresenterCompl(this);
        String cameraId = getIntent().getExtras().getString("cameraId");
        ConstsUtils.CAMERA_ID = cameraId;
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onNetChanged(int netWorkState) {

    }

    public void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Camera2Activity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        CameraHelper.cameraHelper.startBackgroundThread();
        CameraHelper.cameraHelper.initTextureView(this, handler, mTextureView, tvFaceShowCamera2Fragment);
    }

    @Override
    public void onPause() {
        CameraHelper.cameraHelper.closeCamera();
        CameraHelper.cameraHelper.stopBackgroundThread();
        super.onPause();
    }


    @OnClick({R.id.iv_photograph_camera2Fragment, R.id.iv_switchoverCamera_camera2Fragment, R.id.iv_back_camera2Fragment})
    public void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.iv_photograph_camera2Fragment:

//                    if (netWorkState >= 0) {
//                        if (SharedPreferencesUtils.getInstance().getPermission(getString(R.string.storePermissions))) {
                            iCameraPresenter.takePicture(this, handler);
//                        } else {
//                            showToast("当前应用需要打开存储权限.");
//                        }
//                    } else {
//                        showToast("请开启网络");
//                    }
                    break;
                case R.id.iv_switchoverCamera_camera2Fragment:
                    CameraHelper.cameraHelper.cameraSwitch();
                    break;
                case R.id.iv_back_camera2Fragment:
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                    break;
            }
        } else {
            LogUtil.e("多次点击");
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void getPhotoResults(int result) {
        if (result == ConstsUtils.FAIL) {
            LogUtil.d("未检测到人脸");
        } else if (result == ConstsUtils.SUCCEED) {
            LogUtil.d("发送");
            iCameraPresenter.requestContrast(this);
        }
    }

    @Override
    public void getCameraSwitch(boolean flag) {

    }

    @Override
    public void showUploadDialog(boolean flag) {
        if (flag) {
            showProgressDialog();
        } else {
            disMissDialog();
        }
    }

    @Override
    public void showVerificationMsgDialog(String verificationModel) {
        new AlertDialog.Builder(this)
                .setTitle("提示:")
                .setMessage(verificationModel)
                .setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                //按钮事件
                                iCameraPresenter.restartPreview(Camera2Activity.this);
                            }
                        })
                .show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @OnTouch(R.id.iv_photograph_camera2Fragment)
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (view.getId() == R.id.iv_photograph_camera) {
                    ivPhotographCamera.setScaleX(1.2f);
                    ivPhotographCamera.setScaleY(1.2f);
                }
                break;

            case MotionEvent.ACTION_UP:
                if (view.getId() == R.id.iv_photograph_camera) {
                    ivPhotographCamera.setScaleX(1);
                    ivPhotographCamera.setScaleY(1);
                }
                break;
        }
        return false;

    }
}
