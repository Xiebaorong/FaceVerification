package com.hanvon.face;

import android.content.Context;

public class FaceCoreHelper {
    static{
        System.loadLibrary("HWFaceLibSDK");
    }

    public static native String HWGetDeviceCode(Context contex);

    /**
     * 获取秘钥信息
     * @param pbKeyCode 秘钥
     * @param pnKeyCodeSize 秘钥长度
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWGetKeyCode(byte[] pbKeyCode,int[] pnKeyCodeSize, Context contex);

    /**
     * 核心初始化方法
     * @param pHandle 核心句柄
     * @param bpKeyCode 秘钥
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceInitial(byte[] pHandle, byte[] bpKeyCode, Context contex);

    /**
     * 释放核心方法
     * @param pHandle 核心句柄
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceRelease(byte[] pHandle);

    /**
     * 获取模板大小
     * @param pHandle 核心句柄
     * @param featureLength 模板长度
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceGetFeatureSize(byte[] pHandle, int[] featureLength);

    /**
     * 人脸定位方法
     * @param pHandle 核心句柄
     * @param pbImg    传入人脸信息灰度图数据
     * @param width    传入图片宽度
     * @param height   传入图片高度
     * @param pFacePos 输出人脸信息【输入空间有HW_FACEPOS_LEN*pnDetectFaceNum个元素】
     * @param pEyePos  输出人眼信息【输入空间有HW_EYEPOS_LEN*pnDetectFaceNum个元素】
     * @param pnDetectFaceNum    输入最大人脸识别数，输出实际检测到的人脸数
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceDetectFaces(byte[] pHandle, byte[] pbImg, int width, int height, int[] pFacePos, float[] pEyePos, int[] pnDetectFaceNum);

    /**
     * 获取人脸模板
     * @param pHandle 核心句柄
     * @param pbImg    传入人脸信息灰度图数据
     * @param width    传入图片宽度
     * @param height   传入图片高度
     * @param pFacePos    输入人脸信息
     * @param pEyePos     输入人眼信息
     * @param pFeature    输出人脸模板
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceGetFaceFeature(byte[] pHandle, byte[] pbImg, int width, int height, int[] pFacePos, float[] pEyePos, byte[] pFeature);

    /**
     * 人脸比对方法
     * @param pHandle 核心句柄
     * @param thisFeature       当前模板
     * @param referenceFeature  需要比对的模板
     * @param frvalue    比对得分
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceCompareFeature(byte[] pHandle, byte[] thisFeature, byte[] referenceFeature, float[] frvalue);

    /**
     * //如果事先确定是人像图片，比如大头照或身份证照，可以调用该接口 nPortrait = 1，通知核心为人像图片。
     * //这样一些难定位的图片可以更易于定位。
     * //如果不能确定，比如视频截图，则设成 nPortrait = 0.
     * //不调用，核心默认是nPortrait = 0
     * @param pHandle 核心句柄
     * @param iPortrait 1 确定是人像图片。 0 不确定。
     * @return HW_OK = 0，  HW_FAIL = -1
     */
    public static native int HWFaceSetPortrait(byte[] pHandle, int iPortrait);

    /**
     * 通过Rect坐标获取单个人脸信息
     * @param pHandle     传入句柄
     * @param pbImg       传入人脸信息灰度图数据
     * @param width       传入图片宽度
     * @param height      传入图片高度
     * @param pFaceRect   传入RECT坐标【输入空间至少有 4 个元素,RECT{top,bottom,left,right}】
     * @param pFacePos 输出人脸信息【输入空间有HW_FACEPOS_LEN个元素】
     * @param pEyePos  输出人眼信息【输入空间有HW_EYEPOS_LEN个元素】
     * @return
     */
    public static native int HwGetFaceByRect(byte[] pHandle, byte[] pbImg, int width, int height, int[] pFaceRect, int[] pFacePos, float[] pEyePos);

    /**
     * 跟踪器初始化函数
     * 调用除HW_InitTracker外的其他函数前，需要先初始化HW_HANDLE
     * @param pbHandle [输出] 未初始化的HW_HANDLE句柄，请确认 handle = NULL 后再传入，返回初始化后的HW_HANDLE
     * @param trackerParam [输入] 初始化参数，参数均为必填
     * @return 0：成功；-1：失败
     */
    public static native int HwInitTracker(byte[] pbHandle, TrackerParam trackerParam);

    /**
     * 释放跟踪器
     * 当不在使用跟踪器时调用
     * @param pbHandle [输入] 初始化后的HW_HANDLE，释放成功后 handle = NULL
     * @return 0：成功；-1：失败
     */
    public static native int HwReleaseTracker(byte[] pbHandle);

    /**
     * 删除跟踪信息
     * 删除跟踪队列中指定id的跟踪信息
     * @param pbHandle [输入] 初始化后的HW_HANDLE
     * @param nIDCount [输入] 待删除的id个数
     * @param pnID [输入] 待删除的id数组，id数需与id_num一致
     * @return 0：成功；-1：失败
     */
    public static native int HwDropTracker(byte[] pbHandle, int nIDCount, int[] pnID);

    /**
     * 获取跟踪队列大小
     * 返回当前跟踪器的跟踪队列中的目标物体数
     * @param pbHandle [输入] 初始化后的HW_HANDLE
     * @param nTrackerCount [输出] 跟踪队列中的目标物体数
     * @return 0：成功；-1：失败
     */
    public static native int HwGetTrackerSize(byte[] pbHandle, int[] nTrackerCount);

    /**
     * 添加新的目标物体信息
     * 在新增待跟踪目标时使用，增加完毕后会加入跟踪队列中，在调用HW_UpdateTracker函数后返回跟踪结果
     * @param pbHandle [输入] 初始化后的HW_HANDLE
     * @param pbImg [输入] 图像像素信息，像素排列仅支持RGB
     * @param nWidth [输入] 图像宽度
     * @param nHeight [输入] 图像高度
     * @param track_num [输入/输出][输入]在调用前需为track_num赋值当前跟踪队列的长度(如未记录队列长度，可通过HW_GetTrackerSize函数获取)，并申请track_num个pnInfo空间。[输出]返回跟踪后的目标物体数量，数量可能<=调用前（跟丢、置信度低于阀值）
     * @param pnInfo [输入/输出] [输入]在调用前需申请 track_num * 5 空间。[输出]调用后会为前track_num个info赋值跟踪结果，内存空间由调用方释放
     * @param pfInfoConfidence [输入/输出] [输入]在调用前需申请 track_num * 1 空间。[输出]调用后会为前track_num个info赋值跟踪结果，内存空间由调用方释放
     * @return
     */
    public static native int HwInsertTracker(byte[] pbHandle, byte[] pbImg, int nWidth, int nHeight, int track_num, int[] pnInfo, float[] pfInfoConfidence);

    /**
     * 修改跟踪信息
     * 修改跟踪队列中指定id的跟踪位置信息
     * @param pbHandle [输入] 初始化后的HW_HANDLE
     * @param pbImg [输入] 图像像素信息，像素排列仅支持RGB
     * @param nWidth [输入] 图像宽度
     * @param nHeight [输入] 图像高度
     * @param track_num [输入] 待修改目标物体的个数，即info的个数
     * @param pnInfo [输入] 在调用前需申请 track_num * 5 空间, 待修改目标物体的信息，需为rect和id字段赋值，id：待修改目标物体的id，rect：目标物体的新位置。info个数需与track_num一致
     * @param pfInfoConfidence [输入] 在调用前需申请 track_num * 1 空间, info个数需与track_num一致
     * @return
     */
    public static native int HwModifyTracker(byte[] pbHandle, byte[] pbImg, int nWidth, int nHeight, int track_num, int[] pnInfo, float[] pfInfoConfidence);

    /**
     * 跟踪目标物体
     * 在新的一帧图像上跟踪所有目标物体
     * @param pbHandle [输入] 初始化后的HW_HANDLE
     * @param pbImg [输入] 图像像素信息，像素排列仅支持RGB
     * @param nWidth [输入] 图像宽度
     * @param nHeight [输入] 图像高度
     * @param pnTrack_num [输入/输出][输入]在调用前需申请 1 空间 为 pnTrack_num 赋值当前跟踪队列的长度(如未记录队列长度，可通过HW_GetTrackerSize函数获取)，并申请 pnTrack_num[0] 个 Info 空间。[输出]返回跟踪后的目标物体数量，数量可能<=调用前（跟丢、置信度低于阀值）
     * @param pnInfo [输入/输出] [输入]在调用前需申请 track_num * 5 空间。[输出]调用后会为前track_num个info赋值跟踪结果，内存空间由调用方释放
     * @param pfInfoConfidence [输入/输出] [输入]在调用前需申请 track_num * 1 空间。[输出]调用后会为前track_num个info赋值跟踪结果，内存空间由调用方释放
     * @return
     */
    public static native int HwUpdateTracker(byte[] pbHandle, byte[] pbImg, int nWidth, int nHeight, int[] pnTrack_num, int[] pnInfo, float[] pfInfoConfidence);
}
