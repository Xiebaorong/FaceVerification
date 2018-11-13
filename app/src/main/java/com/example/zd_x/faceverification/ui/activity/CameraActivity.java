package com.example.zd_x.faceverification.ui.activity;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseActivity;
import com.example.zd_x.faceverification.callBack.PictureCallBack;
import com.example.zd_x.faceverification.mvp.p.CameraPresenterCompl;
import com.example.zd_x.faceverification.mvp.view.ICameraView;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtils;
import com.hanvon.face.HWCoreHelper;
import com.hanvon.faceRec.Camera2Helper;
import com.hanvon.faceRec.CameraHelper;
import com.hanvon.faceRec.Consts;
import com.hanvon.faceRec.HWConsts;
import com.hanvon.faceRec.HWFaceIDCardCompareLib;
import com.hanvon.faceRec.HanvonfaceCamera2ShowView;
import com.hanvon.faceRec.HanvonfaceCameraShowView;

import butterknife.BindView;
import butterknife.OnClick;

public class CameraActivity extends BaseActivity implements ICameraView {
    private static final String TAG = "CameraActivity";
    @BindView(R.id.iv_photograph_camera)
    ImageView ivPhotographCamera;
    @BindView(R.id.iv_switchoverCamera_camera)
    ImageView ivSwitchoverCameraCamera;
    //SDK 5.0
//    @BindView(R.id.hcsv_cameraPreview_camera)
//    HanvonfaceCameraShowView hanvonfaceCameraShowView;
    //SDK 7.0
    @BindView(R.id.hcsv_camera2Preview_camera)
    HanvonfaceCamera2ShowView hcsvCamera2PreviewCamera;

    @BindView(R.id.sfv_faceShow_camera)
    SurfaceView sfvFaceShowCamera;

    private CameraPresenterCompl iCameraPresenter;
    private String cameraId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Consts.INIT_SUCCESS:
                    HWCoreHelper.initHWCore(CameraActivity.this, handler);
                    break;
                case Consts.SHOW_MSG:

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
        cameraId = getIntent().getExtras().getString("cameraId");

        ConstsUtils.CAMERA_ID = cameraId;
        int result = Camera2Helper.camera2Helper.initCamera(this);
        if (result == ConstsUtils.OK) {
            Camera2Helper.camera2Helper.openCamera(this);
        }
//        if (!CameraHelper.cameraHelper.isCameraOpen()) {
//            HWFaceIDCardCompareLib.getInstance().setRotation(CameraHelper.cameraHelper.initCamera(this, cameraId));
//        }
    }

    @Override
    protected void initEvent() {
//        hanvonfaceCameraShowView.setSurfaceView(sfvFaceShowCamera, handler);
//        HWCoreHelper.initHWCore(this, handler);
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
            showToast("未检测到人脸");
            LogUtils.d(TAG, "未检测到人脸");
        } else if (result == ConstsUtils.SUCCEED) {
            LogUtils.d(TAG, "发送");

        }
    }

    @Override
    public void getCameraSwitch(boolean flag) {
        if (flag) {
//            hanvonfaceCameraShowView.setSurfaceView(sfvFaceShowCamera, handler);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SDK5.0
//        CameraHelper.cameraHelper.closeCamera();
//        hanvonfaceCameraShowView = null;
//        HWCoreHelper.releaseCore();
//        HanvonfaceCameraShowView.hanvonfaceShowView = null;
        //SDK7.0
        hcsvCamera2PreviewCamera = null;
        HanvonfaceCamera2ShowView.hanvonfaceShowView = null;
        Camera2Helper.camera2Helper.stopCamera();
        handler.removeCallbacksAndMessages(null);
    }


}
