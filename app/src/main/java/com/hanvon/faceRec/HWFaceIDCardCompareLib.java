package com.hanvon.faceRec;


import android.media.Image;
import android.os.Handler;
import android.util.Log;

import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.hanvon.face.FaceCoreHelper;
import com.hanvon.face.HWCoreHelper;
import com.hanvon.face.HWFaceClient;

import static com.hanvon.face.HWCoreHelper.FACE_HANDLER;


public class HWFaceIDCardCompareLib {
    private static HWFaceIDCardCompareLib mInstance;
    private String TAG = "HWFaceIDCardCompareLib";
    public int iWidth = 0;
    public int iHeight = 0;
    private int mRotation = 0;    //翻转角度
    public FaceRect objRect = new FaceRect(); // 输出人脸坐标
    private int[] nFaceCount = new int[]{HWConsts.iFaceMax};  // 最大检测到的人脸个数

    private enumRotation eCameraAngles = enumRotation.Rotation_0; // 摄像头角度
    private int iFacePosLength = HWConsts.HW_FACEPOS_LEN + HWConsts.HW_FACEINFO_LEN; // 单个人脸长度

    //公开枚举

    /**
     * 顺时针方向
     * 旋转0度
     * 旋转90度
     * 旋转180度
     * 旋转270度
     */
    public enum enumRotation {
        Rotation_0,
        Rotation_90,
        Rotation_180,
        Rotation_270,
    }

    public void setRotation(int mRotation) {
        switch (mRotation) {
            case 0:
                eCameraAngles = enumRotation.Rotation_0;
                break;
            case 90:
                eCameraAngles = enumRotation.Rotation_90;
                break;
            case 180:
                eCameraAngles = enumRotation.Rotation_180;
                break;
            case 270:
                eCameraAngles = enumRotation.Rotation_270;
                break;
        }

    }

    public HWFaceIDCardCompareLib() {
        super();
    }

    public static HWFaceIDCardCompareLib getInstance() {
        return mInstance == null ? (mInstance = new HWFaceIDCardCompareLib()) : mInstance;
    }

    public void setDefaultFacePoint() {
        if (null == objRect) {
            return;
        }
        objRect.iTop = 0;
        objRect.iBottom = 0;
        objRect.iLeft = 0;
        objRect.iRight = 0;
//        HwitManager.HwitSetCameraX7022ExposureRange(0, 0, 0, 0 );

    }
    int Result;
    public int SetCameraPreviewFrame(byte[] data) {

        int[] faceNum = new int[1];
        int intFaceNum = 1;
        faceNum[0] = intFaceNum;    // 单人脸
        int width = Camera2Helper.PIXEL_WIDTH;
        int height = Camera2Helper.PIXEL_HEIGHT;
        byte[] rotateData = new byte[data.length];

        int[] pFacePos = new int[(HWFaceClient.HW_FACEPOS_LEN) * intFaceNum];
        float[] pEyePos = new float[(HWFaceClient.HW_EYEPOS_LEN) * intFaceNum];

        if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
            width = Camera2Helper.PIXEL_HEIGHT;
            height = Camera2Helper.PIXEL_WIDTH;
            UtilFunc.rotateYuvData(rotateData, data, Camera2Helper.PIXEL_WIDTH, Camera2Helper.PIXEL_HEIGHT, 1);
        }else if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.REAR_CAMERA))) {

        }

        long startLocatTime = System.nanoTime();
        Result = FaceCoreHelper.HWFaceDetectFaces(FACE_HANDLER, data, width, height, pFacePos, pEyePos, faceNum);
        long locatTime = (System.nanoTime() - startLocatTime)/1000000;
        Log.e(TAG, "SetCameraPreviewFrame: iResult: " + Result);
        Log.e("Tracker", " 定位耗时 " + locatTime );

        if (Result == HWConsts.HW_OK) {
            objRect.iTop = pFacePos[0];
            objRect.iBottom = pFacePos[1];
            objRect.iLeft = pFacePos[2];
            objRect.iRight = pFacePos[3];
        } else {
            setDefaultFacePoint();
        }

        return Result;
    }

}

