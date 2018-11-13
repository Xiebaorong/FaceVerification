package com.hanvon.face;

import android.content.Context;

public class FaceCoreHelper {
    static {
        System.loadLibrary("HWFaceLibSDK");
    }

    /**
     * 获取秘钥信息
     *
     * @param pbKeyCode     秘钥
     * @param pnKeyCodeSize 秘钥长度
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWGetKeyCode(byte[] pbKeyCode, int[] pnKeyCodeSize, Context contex);

    /**
     * 核心初始化方法
     *
     * @param pHandle   核心句柄
     * @param bpKeyCode 秘钥
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceInitial(byte[] pHandle, byte[] bpKeyCode, Context contex);

    /**
     * 释放核心方法
     *
     * @param pHandle 核心句柄
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceRelease(byte[] pHandle);

    /**
     * 获取模板大小
     *
     * @param pHandle       核心句柄
     * @param featureLength 模板长度
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceGetFeatureSize(byte[] pHandle, int[] featureLength);

    /**
     * 人脸定位方法
     *
     * @param pHandle         核心句柄
     * @param pbImg           传入人脸信息灰度图数据
     * @param width           传入图片宽度
     * @param height          传入图片高度
     * @param pFacePos        输出人脸信息【输入空间有HW_FACEPOS_LEN*pnDetectFaceNum个元素】
     * @param pEyePos         输出人眼信息【输入空间有HW_EYEPOS_LEN*pnDetectFaceNum个元素】
     * @param pnDetectFaceNum 输入最大人脸识别数，输出实际检测到的人脸数
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceDetectFaces(byte[] pHandle, byte[] pbImg, int width, int height, int[] pFacePos, float[] pEyePos, int[] pnDetectFaceNum);

    /**
     * 获取人脸模板
     *
     * @param pHandle  核心句柄
     * @param pbImg    传入人脸信息灰度图数据
     * @param width    传入图片宽度
     * @param height   传入图片高度
     * @param pFacePos 输入人脸信息
     * @param pEyePos  输入人眼信息
     * @param pFeature 输出人脸模板
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceGetFaceFeature(byte[] pHandle, byte[] pbImg, int width, int height, int[] pFacePos, float[] pEyePos, byte[] pFeature);

    /**
     * 人脸比对方法
     *
     * @param pHandle          核心句柄
     * @param thisFeature      当前模板
     * @param referenceFeature 需要比对的模板
     * @param frvalue          比对得分
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceCompareFeature(byte[] pHandle, byte[] thisFeature, byte[] referenceFeature, float[] frvalue);

    /**
     * //如果事先确定是人像图片，比如大头照或身份证照，可以调用该接口 nPortrait = 1，通知核心为人像图片。
     * //这样一些难定位的图片可以更易于定位。
     * //如果不能确定，比如视频截图，则设成 nPortrait = 0.
     * //不调用，核心默认是nPortrait = 0
     *
     * @param pHandle   核心句柄
     * @param iPortrait 1 确定是人像图片。 0 不确定。
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceSetPortrait(byte[] pHandle, int iPortrait);

    /**
     * @param context
     * @return 设备ID
     */
    public static native String HWGetDeviceCode(Context context);
}
