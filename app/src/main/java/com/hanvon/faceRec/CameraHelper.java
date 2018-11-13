package com.hanvon.faceRec;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import com.example.zd_x.faceverification.utils.ConstsUtils;

import java.io.IOException;
import java.util.List;

public class CameraHelper {
    private String TAG = "CameraHelper";
    public static int CAMERA_FACING = Camera.CameraInfo.CAMERA_FACING_BACK;
    // 相机预览分辨率
    public static int PIXEL_WIDTH = 1920;// 1920,1280,(1280),(960),(640),576,480,384,352
    public static int PIXEL_HEIGHT = 1080;// 1080,720,(960),(720),(480),432,320,288,288
    public static CameraHelper cameraHelper = new CameraHelper();
    private Camera mCamera = null;
    Camera.Parameters parameters;

    public boolean isCameraOpen() {
        if (mCamera != null) {
            return true;
        }
        return false;
    }

    static boolean isPreView = false;

    private int mOpenedCamera = 0;
    private int mRotation = 0;    //翻转角度

    public int initCamera(Activity mContext, int CameraId) {
        mOpenedCamera = CameraId;
        mCamera = Camera.open(CameraId);//mOpenedCamera = 0;
        if (mCamera != null) {
            parameters = mCamera.getParameters();
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setPreviewFormat(ImageFormat.NV21);
            parameters.setJpegQuality(95);
            /* 指定preview的屏幕大小 */
            parameters.setPreviewSize(PIXEL_WIDTH, PIXEL_HEIGHT);
            List<String> list = parameters.getSupportedAntibanding();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals(Camera.Parameters.ANTIBANDING_50HZ)) {
                        parameters.setAntibanding(Camera.Parameters.ANTIBANDING_50HZ);
                    }
                }
            }
            list = parameters.getSupportedWhiteBalance();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).equals(Camera.Parameters.WHITE_BALANCE_AUTO)) {
                        parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
                    }
                }
            }
            try {
                mCamera.setParameters(parameters);
                setCameraPreviewOrientation(mContext, mOpenedCamera, mCamera); //FIXME: remove hardcode '0' here
            } catch (Exception e) {
            }
        }
        return mRotation;
    }

    public int setEV(int EV) {
        parameters.setExposureCompensation(EV);
        mCamera.setParameters(parameters);
        return parameters.getExposureCompensation();
    }

    //预览
    public void startPreview(SurfaceHolder holder, Camera.PreviewCallback pb) {

        if (null != mCamera && !isPreView) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.setPreviewCallback(pb);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
            isPreView = true;
        }
    }

    /**
     * 拍照
     */
    public void takePicture(final Context context, Camera.PictureCallback pictureCallback) {
        mCamera.takePicture(new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
            }
        }, null, pictureCallback);

    }

    public Camera cameraSwitch() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
            if (mOpenedCamera == ConstsUtils.FRONT_CAMERA) {
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    closeCamera();
                    mOpenedCamera = ConstsUtils.REAR_CAMERA;
                    mCamera = Camera.open(mOpenedCamera);
                }
            }else {
                if (cameraInfo.facing==Camera.CameraInfo.CAMERA_FACING_FRONT){
                    closeCamera();
                    mOpenedCamera = ConstsUtils.FRONT_CAMERA;
                    mCamera = Camera.open(mOpenedCamera);
                }
            }
        }
        return mCamera;
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
            isPreView = false;
        }
    }

    /* func:停止preview,释放Camera对象 */
    public void closeCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            stopPreview();
//            mCamera.stopPreview();
            /* 释放Camera对象 */
            mCamera.release();
            mCamera = null;
            Log.e(TAG, "111232-closeCamera: stop");
        }
    }


    /**
     * 设置旋转角度
     *
     * @param activity
     * @param cameraId
     * @param camera
     */
    private void setCameraPreviewOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mRotation = result;
        camera.setDisplayOrientation(result);
//	     mRotation = info.orientation;

    }


}
