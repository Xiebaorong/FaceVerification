package com.hanvon.faceRec;


import android.media.Image;
import android.os.Handler;
import android.util.Log;

import com.hanvon.face.FaceCoreHelper;
import com.hanvon.face.HWCoreHelper;


public class HWFaceIDCardCompareLib {
    private static HWFaceIDCardCompareLib mInstance;
    private String TAG = "HWFaceIDCardCompareLib";
    public int iWidth = 0;
    public int iHeight = 0;

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

    public void SetCameraPreviewFrame(byte[] Data, int Width, int Height, Handler mHandler) {
        iWidth = Width;
        iHeight = Height;
        int dataLen = Data.length;
        int[] nFacePosition = new int[HWConsts.iFaceMax * iFacePosLength]; // 人脸信息 = 单个人脸长度 * 人脸个数
        byte[] bpRotateData = new byte[dataLen];
        float[] faceEyes = new float[6];
        nFaceCount[0] = HWConsts.iFaceMax;
        switch (eCameraAngles) {
            case Rotation_0:
                System.arraycopy(Data, 0, bpRotateData, 0, dataLen);
                break;
            case Rotation_90:
                UtilFunc.rotateYUV240SP_Clockwise(Data, bpRotateData, iWidth, iHeight);
                iWidth = Height;
                iHeight = Width;
                break;
            case Rotation_180:
                UtilFunc.rotateYUV240SP_FlipY180(Data, bpRotateData, iWidth, iHeight);
                break;
            case Rotation_270:
                UtilFunc.rotateYUV240SP_AntiClockwise(Data, bpRotateData, iWidth, iHeight);
                iWidth = Height;
                iHeight = Width;
                break;
            default:
                System.arraycopy(Data, 0, bpRotateData, 0, dataLen);
                break;
        }
        int iResult = FaceCoreHelper.HWFaceDetectFaces(HWCoreHelper.FACE_HANDLER, bpRotateData, iWidth, iHeight, nFacePosition, faceEyes, nFaceCount);
        Log.e(TAG, "SetCameraPreviewFrame: iResult: "+iResult );
//        if (iResult == HWConsts.HW_OK) {
////            mHandler.sendMessage(mHandler.obtainMessage(Consts.SHOW_MSG, "人脸识别已开启"));
//            objRect.iTop = nFacePosition[0];
//            objRect.iBottom = nFacePosition[1];
//            objRect.iLeft = nFacePosition[2];
//            objRect.iRight = nFacePosition[3];
//        } else {
//            setDefaultFacePoint();
//        }
    }

}

