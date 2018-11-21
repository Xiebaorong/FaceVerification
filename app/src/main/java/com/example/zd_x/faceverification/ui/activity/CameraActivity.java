package com.example.zd_x.faceverification.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.mvp.p.compl.CameraPresenterCompl;
import com.example.zd_x.faceverification.mvp.view.ICameraView;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtil;
import com.hanvon.face.HWCoreHelper;
import com.hanvon.faceRec.Camera2Helper;
import com.hanvon.faceRec.HanvonfaceCamera2ShowView;

import butterknife.BindView;
import butterknife.OnClick;

public class CameraActivity extends BaseActivity implements ICameraView {
    private static final String TAG = "CameraActivity";
    @BindView(R.id.iv_photograph_camera)
    ImageView ivPhotographCamera;
    @BindView(R.id.iv_switchoverCamera_camera)
    ImageView ivSwitchoverCameraCamera;
    //SDK 7.0
    @BindView(R.id.hcsv_camera2Preview_camera)
    HanvonfaceCamera2ShowView hcsvCamera2PreviewCamera;
    @BindView(R.id.sfv_faceShow_camera)
    SurfaceView sfvFaceShowCamera;

    private CameraPresenterCompl iCameraPresenter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ConstsUtils.INIT_SUCCESS:
                    Log.e(TAG, "handleMessage: -----");
                    HWCoreHelper.initHWCore(CameraActivity.this, handler);

                    break;
                case ConstsUtils.SHOW_MSG:
                    showToast((String) msg.obj);
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
        HWCoreHelper.initHWCore(this, handler);
    }

    @Override
    protected void initEvent() {
        Camera2Helper.camera2Helper.initCamera(this);
        hcsvCamera2PreviewCamera.setSurfaceView(sfvFaceShowCamera, handler);
    }


    @OnClick({R.id.iv_photograph_camera, R.id.iv_switchoverCamera_camera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_photograph_camera:
                iCameraPresenter.takePicture(this);
                break;
            case R.id.iv_switchoverCamera_camera:
                iCameraPresenter.cameraSwitch(this);
                break;
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
        if (flag) {
        }
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
        super.onBackPressed();
        Camera2Helper.camera2Helper.isCanDetectFace= false;
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
        Camera2Helper.camera2Helper.isCanDetectFace= true;
    }


}
