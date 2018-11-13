package com.hanvon.faceRec;

import android.graphics.Bitmap;

public class CameraDataHelp {
	static{
		System.loadLibrary("HWCameraData");
	}
	
	public static native int YUV2RGB(byte[] data,int width,int height,int[] rgba);
	public static native void RGB2GRAY(int[] pixls,int w,int h,byte[]graybyte);
	public static native void getNdkBitmap(int[] pixls,int w,int h,int[]graybyte);
	public static native void convertToGray(Bitmap srcBitmap, Bitmap grayBitmap);
	
	
	

}
