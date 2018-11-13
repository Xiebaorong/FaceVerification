package com.hanvon.face;


import android.graphics.Bitmap;

import com.hanvon.faceRec.CameraDataHelp;
import com.hanvon.faceRec.HWConsts;


public class HWFaceCommonUtil {
    private static String TAG = "HWFaceCommonUtil";

    /**
     * 获取BMP模板数据
     *
     * @param bmp Bitmap图像对象
     * @return 模板数据
     */
    public static byte[] getFeature(Bitmap bmp) {
        int result = HWConsts.HW_FAIL;
        byte[] pbFeature = null;
        int iWidth = bmp.getWidth();
        int iHeight = bmp.getHeight();
        byte[] referenceFeature = new byte[iWidth * iHeight];
        int[] pixels = new int[iWidth * iHeight]; //通过位图的大小创建像素点数组
        bmp.getPixels(pixels, 0, iWidth, 0, 0, iWidth, iHeight);
        CameraDataHelp.RGB2GRAY(pixels, iWidth, iHeight, referenceFeature);
        int[] detectNum = new int[]{1};
        int[] facePos = new int[(HWConsts.HW_FACEINFO_LEN + HWConsts.HW_FACEPOS_LEN) * detectNum[0]];
        float[] faceEye = new float[6];
        int detectResult = FaceCoreHelper.HWFaceDetectFaces(HWCoreHelper.CARD_HANDLER, referenceFeature, iWidth, iHeight, facePos, faceEye,detectNum);
        if(detectResult == HWConsts.HW_OK){
			pbFeature = new byte[HWConsts.iFeatureSize];
			result = FaceCoreHelper.HWFaceGetFaceFeature(HWCoreHelper.CARD_HANDLER, referenceFeature, iWidth, iHeight, facePos,faceEye,pbFeature);
			if(result != HWConsts.HW_OK){
				pbFeature = null;
			}
		}
        return pbFeature;
    }

    // 根据人脸特征坐标抠出人脸图片 左上右下
    public static Bitmap getFaceImgByInfraredJpg(int left, int top, int right, int bottom, Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        if (top != bottom && left != right) {
            int iFaceWidth = (right - left) * 2;
            if (iFaceWidth >= width) {
                iFaceWidth = width - 10;
            }
            int iFaceHeight = (bottom - top) * 3;
            if (iFaceHeight >= height) {
                iFaceHeight = height - 10;
            }
            int iLeft = left + (right - left) / 2 - iFaceWidth / 2;
            iLeft = iLeft > 0 ? iLeft : 0;
            int iTop = top + (bottom - top) / 2 - iFaceHeight / 2;
            iTop = iTop > 0 ? iTop : 0;
            if (iLeft < width && iTop < height) {
                int iWidth = 0;
                int iHeight = 0;
                if (width < (iLeft + iFaceWidth)) {
                    iWidth = width - iLeft - 10;
                } else {
                    iWidth = iFaceWidth;
                }
                if (height < (iTop + iFaceHeight)) {
                    iHeight = height - iTop - 10;
                } else {
                    iHeight = iFaceHeight;
                }
                int oldW = iWidth;
                iWidth = (int) ((81.0f / 111.0f) * (float) iHeight);
                iLeft = iLeft + ((oldW / 2) - iWidth / 2);
                iLeft = iLeft > 0 ? iLeft : 0;
                if (iLeft + iWidth >= bmp.getWidth()) {
                    iWidth = bmp.getWidth() - iLeft - 5;
                }
                return Bitmap.createBitmap(bmp, iLeft, iTop, iWidth, iHeight);
            }
        }
        return null;
    }
}
