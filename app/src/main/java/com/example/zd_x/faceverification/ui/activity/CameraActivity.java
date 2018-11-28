package com.example.zd_x.faceverification.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.application.FaceVerificationApplication;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.mvp.p.compl.CameraPresenterCompl;
import com.example.zd_x.faceverification.mvp.view.ICameraView;
import com.example.zd_x.faceverification.utils.ButtonUtils;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtil;
import com.example.zd_x.faceverification.utils.SharedPreferencesUtils;
import com.hanvon.face.HWCoreHelper;
import com.hanvon.faceRec.Camera2Helper;
import com.hanvon.faceRec.HanvonfaceCamera2ShowView;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTouch;

public class CameraActivity extends BaseActivity implements ICameraView {
    private static final String TAG = "CameraActivity";
    @BindView(R.id.iv_photograph_camera)
    ImageView ivPhotographCamera;
    @BindView(R.id.iv_switchoverCamera_camera)
    ImageView ivSwitchoverCameraCamera;
    @BindView(R.id.iv_back_camera)
    ImageView ivBackCamera;
    //SDK 7.0
    @BindView(R.id.hcsv_camera2Preview_camera)
    HanvonfaceCamera2ShowView hcsvCamera2PreviewCamera;
    @BindView(R.id.sfv_faceShow_camera)
    SurfaceView sfvFaceShowCamera;
    @BindView(R.id.tv_hintText_camera)
    TextView tvHintTextCamera;
    @BindView(R.id.tv_hint_network)
    TextView tHintNetwork;
    private int netWorkState;
    private CameraPresenterCompl iCameraPresenter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstsUtils.INIT_SUCCESS:
                    HWCoreHelper.initHWCore(FaceVerificationApplication.getmApplication(), handler);
                    break;
                case ConstsUtils.SHOW_MSG:
                    tvHintTextCamera.setText((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void OnActCreate(Bundle savedInstanceState) {
        iCameraPresenter = new CameraPresenterCompl(this);
        String cameraId = getIntent().getExtras().getString("cameraId");
        ConstsUtils.CAMERA_ID = cameraId;


    }

    @Override
    protected void initEvent() {
        Camera2Helper.camera2Helper.initCamera(this);
        hcsvCamera2PreviewCamera.setSurfaceView(sfvFaceShowCamera, handler);
    }

    @Override
    protected void onNetChanged(int state) {
        netWorkState = state;
        if (state < 0) {
            tHintNetwork.setVisibility(View.VISIBLE);
        } else {
            tHintNetwork.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: " + netWorkState);
                HWCoreHelper.initHWCore(FaceVerificationApplication.getmApplication(), handler);
            }
        }, 500);
    }

    @OnClick({R.id.iv_photograph_camera, R.id.iv_switchoverCamera_camera, R.id.iv_back_camera})
    public void onClick(View view) {
        if (!ButtonUtils.isFastDoubleClick()) {
            switch (view.getId()) {
                case R.id.iv_photograph_camera:
                    if (netWorkState >= 0) {
                        if (SharedPreferencesUtils.getInstance().getPermission(getString(R.string.storePermissions))) {
                            iCameraPresenter.takePicture(this, handler);
                        } else {
                            showToast("当前应用需要打开存储权限.");
                        }
                    } else {
                        showToast("请开启网络");
                    }
                    break;
                case R.id.iv_switchoverCamera_camera:
                    iCameraPresenter.cameraSwitch(this);
                    break;
                case R.id.iv_back_camera:
                    Camera2Helper.camera2Helper.isCanDetectFace = false;
                    startActivity(new Intent(this, HomeActivity.class));
                    finish();
                    break;
            }
        } else {
            LogUtil.e("多次点击");
        }
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
    public void showVerificationMsgDialog(String msg) {
        //android.view.WindowManager$BadTokenException: Unable to add window -- token android.os.BinderProxy@fe70341 is not valid; is your activity running?
        new AlertDialog.Builder(this)
                .setTitle("提示:")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialoginterface, int i) {
                                //按钮事件
                                iCameraPresenter.restartPreview(CameraActivity.this);
                            }
                        })
                .show();

    }

    @Override
    public void onBackPressed() {
        Camera2Helper.camera2Helper.isCanDetectFace = false;
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SDK7.0
        HWCoreHelper.releaseCore();
        Camera2Helper.camera2Helper.stopCamera();
        handler.removeCallbacksAndMessages(null);
        HanvonfaceCamera2ShowView.hanvonfaceShowView = null;
        hcsvCamera2PreviewCamera = null;
        Camera2Helper.camera2Helper.isCanDetectFace = true;
    }

    @OnTouch(R.id.iv_photograph_camera)
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
