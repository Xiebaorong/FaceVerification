package com.hanvon.faceRec;

public class HWConsts {

	/**
	 * 视频高度
	 */
	public static int iWidth = 640;
	/**
	 * 视频高度
	 */
	public static int iHeight = 480;
	/**
	* 成功
	*/
    public final static int HW_OK = 0;
	/**
	* 失败
	*/
    public final static int HW_FAIL = -1;
    /**
  	 * 人脸信息数据长度
  	 */
  	public final static int HW_FACEINFO_LEN = 224;
  	/**
  	 * 人脸坐标数据长度
  	 */
  	public final static int HW_FACEPOS_LEN = 4;
	/**
	 * 失败次数
	 */
	public static int iFailCount = 0;
	/**
	 * 失败最大次数
	 */
	public static int iFailMax = 2;
	/**
	 * 可见光模板长度
	 */
	public static int iFeatureSize = 4000;
	/**
	 * iFaceMax 1 单帧检测最多人脸个数
	 */
	public static int iFaceMax = 1;
	/**
	 * 可见光比对阈值
	 */
	public static float fScore = 0.65f;
}
