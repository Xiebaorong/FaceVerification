package com.hanvon.faceRec;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UtilFunc {
	
	public static void rotateYUV240SP_Clockwise(byte[] src,byte[] des,int width,int height){          
		int wh = width * height;  
		//旋转Y   
		int k = 0;  
		for(int i=0;i<width;i++){  
			for(int j=0;j<height;j++){  
				des[k] = src[width*(height-j-1) + i];              
				k++;  
			}  
		}  
//		for(int i=0;i<width;i+=2) {
//			for(int j=0;j<height/2;j++){
//				des[k] = src[wh+ width*(height/2-j-1) + i];
//				des[k+1]=src[wh + width*(height/2-j-1) + i+1];
//				k+=2;
//			}
//		}
	}

	public static void saveFile(final String dest, final byte[] bmp) {
		if(bmp != null){
			final File f = new File(dest);
			if (f.exists()) {
				f.delete();
			}
			try {
				final FileOutputStream out = new FileOutputStream(f);
				out.write(bmp);
				out.flush();
				out.close();
			} catch (final FileNotFoundException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void rotateYUV240SP_AntiClockwise(byte[] src,byte[] des,int width,int height){
		int wh = width * height;  
		//旋转Y   
		int k = 0;  
		for(int i=0;i<width;i++) {  
			for(int j=0;j<height;j++){  
				des[k] = src[width*j + width-i-1];
				k++;  
			}  
		}  

//		for(int i=0;i<width;i+=2) {
//			for(int j=0;j<height/2;j++){
//				des[k+1] = src[wh+ width*j + width-i-1];
//				des[k]=src[wh + width*j + width-(i+1)-1];
//				k+=2;
//			}
//		}

	}

    public static byte[] rotateYUV420Degree270(byte[] ydata, byte[] udata, byte[] vdata, int imageWidth,
                                               int imageHeight) {
        byte[] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;
        for (int x = imageWidth - 1; x >= 0; x--) {
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = ydata[y * imageWidth + x];
                i++;
            }
        }// Rotate the U and V color components
        i = imageWidth * imageHeight;
        for (int x = imageWidth - 1; x > 0; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = udata[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i++;
                yuv[i] = vdata[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i++;
            }
        }
        return yuv;
    }

	public static void rotateYUV240SP_FlipY180(byte[] src,byte[] des,int width,int height){  	         
		int wh = width * height;  
		//旋转Y   
		int k = 0;  
		for(int i=0;i<height;i++) {  
			for(int j=0;j<width;j++){  
				des[k] = src[width*(height-i-1) + j];              
				k++;  
			}  
		}  	     
		for(int i=0;i<height/2;i++) {  
			for(int j=0;j<width;j+=2){     
				des[k]= src[wh + width*(height/2 -i-1) + j];  
				des[k+1] = src[wh+ width*(height/2 -i-1) + j+1];   		                 
				k+=2;  
			}  
		} 

	}
	//it works becuase in YCbCr_420_SP and YCbCr_422_SP, the Y channel is planar and appears first
	public static void rotateYuvData(byte[] rotatedData, byte[] data, int width, int height,int nCase){
		if( nCase == 0){
			rotateYUV240SP_Clockwise(data,rotatedData,width,height);
		}else if(nCase == 2){    		
			rotateYUV240SP_FlipY180(data,rotatedData,width,height);
		}else{
			rotateYUV240SP_AntiClockwise(data,rotatedData,width,height);
		}

	} 

	/**
	 *    RGB32转灰度
	 * @param srcImgData   输入RGB32数据
	 * @param nWidth
	 * @param nHeight
	 * @param pbGrayData    输出灰度数据
	 */
	public static void getGrayDataFromRgb32(int[] srcImgData,int nWidth,int nHeight,byte[] pbGrayData){
		Bitmap srcColor = Bitmap.createBitmap(srcImgData,nWidth, nHeight, Bitmap.Config.ARGB_8888);
		int w=srcColor.getWidth();
		int h=srcColor.getHeight();
		int[] pix = new int[w * h];
		srcColor.getPixels(pix, 0, w, 0, 0, w, h);
		int alpha=0xFF<<24;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				// 获得像素的颜色
				int color = pix[w * i + j];
				int red = ((color & 0x00FF0000) >> 16);
				int green = ((color & 0x0000FF00) >> 8);
				int blue = color & 0x000000FF;
				color = (red + green + blue)/3;
				color = alpha | (color << 16) | (color << 8) | color;
				pbGrayData[w * i + j] = (byte)color;
			}
		}
		pix = null;
		if(!srcColor.isRecycled()){
			srcColor.recycle();
		}	
	}

	public static Bitmap toImgScale(Bitmap bitmap, int nWidth, int nHeight) {
		//2、得到以上加载图片的高度跟宽度 
		int height = bitmap.getHeight();
		int width = bitmap.getWidth(); 
		//3、定义要缩放成最终的图片高度跟宽度           
		//  int nWidth = 640; 
		//4、计算缩放比例
		float scaleWidth = ((float) nWidth)/width;
		float scaleHeight = ((float) nHeight)/height;
		//5、创建Matrix对象 Matrix是在Android中用于操作图像的类 
		Matrix matrix = new Matrix();
		//6、使用Matrix对象跟缩放比例实现缩放图片  
		matrix.postScale(scaleWidth, scaleHeight);  
		//同样的，图片旋转只需要通过Matrix改变图片角度即可，生成图片跟7相同。  
		//7、生成缩放后的图片  
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,width, height, matrix, true);
		return resizedBitmap;
	}

}
