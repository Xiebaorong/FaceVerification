package com.hanvon.faceRec;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.example.zd_x.faceverification.application.FaceVerificationApplication;
import com.example.zd_x.faceverification.callBack.PictureCallBack;
import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class HanvonfaceCamera2ShowView extends TextureView implements TextureView.SurfaceTextureListener {
    private static final String TAG = "HanvonfaceCamera2ShowVi";
    public static HanvonfaceCamera2ShowView hanvonfaceShowView = null;
    private Handler mHandler;
    private SurfaceView mSurfaceView02;//mSurfaceHolder02 显示框 对应SurfaceView
    private SurfaceHolder mSurfaceHolder02;

    public HanvonfaceCamera2ShowView(Context context) {
        super(context);
        hanvonfaceShowView = this;

    }

    public HanvonfaceCamera2ShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        hanvonfaceShowView = this;
        startListener();
    }

    public HanvonfaceCamera2ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        hanvonfaceShowView = this;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Camera2Helper.camera2Helper.takePreview(this, onImageAvailableListener);

    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public boolean getAvailable() {
        if (this.isAvailable()) {
            Camera2Helper.camera2Helper.takePreview(this, onImageAvailableListener);
        }
        return this.isAvailable();
    }

    public void startListener() {
        setSurfaceTextureListener(this);
    }

    /**
     * 设置显示和绘制SurfaceView
     *
     * @param view02 绘制 Surface
     */
    public void setSurfaceView(SurfaceView view02, Handler handler) {
        this.mHandler = handler;
        mSurfaceView02 = view02;
        mSurfaceView02.setZOrderOnTop(true);
        getSurfaceHolder();

    }

    public void getSurfaceHolder() {
        mSurfaceHolder02 = mSurfaceView02.getHolder();
        mSurfaceHolder02.setFormat(PixelFormat.TRANSLUCENT);
    }


    private final ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            synchronized (this) {
                try {
                    Image image;
                    image = reader.acquireLatestImage();
                    if (image == null) {
                        return;
                    }
                    ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    int dataLen = data.length;
                    byte[] newData = new byte[dataLen];
                    System.arraycopy(data, 0, newData, 0, dataLen);
                    image.close();

                    cameraPreview(newData);
                } catch (Exception e) {
                    Log.e(TAG, "onImageAvailable: " + e.getMessage());
                }

            }

        }
    };

    private void cameraPreview(final byte[] newData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                HWFaceIDCardCompareLib.getInstance().SetCameraPreviewFrame(newData, Camera2Helper.PIXEL_WIDTH, Camera2Helper.PIXEL_HEIGHT, mHandler);
//                canvasDarwLine(HWFaceIDCardCompareLib.getInstance().iWidth, HWFaceIDCardCompareLib.getInstance().iHeight);
            }
        }).start();
    }


    private void canvasDarwLine(int iWidth, int iHeight) {
        Canvas canvas = mSurfaceHolder02.lockCanvas(null);
        try {
            if (null != canvas) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                Paint mPaint = new Paint();
                mPaint.setColor(0xff00FFFF);// 画笔为绿色
                mPaint.setStrokeWidth(2);// 设置画笔粗细
                mPaint.setTextSize(16.0f);

                if (HWFaceIDCardCompareLib.getInstance().objRect.iTop > 0) {
                    int startX = HWFaceIDCardCompareLib.getInstance().objRect.iLeft;
                    int startY = HWFaceIDCardCompareLib.getInstance().objRect.iTop;
                    int stopX = HWFaceIDCardCompareLib.getInstance().objRect.iRight;
                    int stopY = HWFaceIDCardCompareLib.getInstance().objRect.iBottom;
                    //镜像
                    if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.REAR_CAMERA))) {
                        startX = iWidth - startX;
                        stopX = iWidth - stopX;
                    }

                    int heightf = mSurfaceHolder02.getSurfaceFrame().height();
                    int widthf = mSurfaceHolder02.getSurfaceFrame().width();
                    float hratio = (float) heightf / (float) iHeight;
                    float wratio = (float) widthf / (float) iWidth;
                    //添加 比例换算
                    startX = (int) ((float) startX * wratio);
                    startY = (int) ((float) startY * hratio);
                    stopX = (int) ((float) stopX * wratio);
                    stopY = (int) ((float) stopY * hratio);
                    int iPaintSize = (stopY - startY) / 6;


                    if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.REAR_CAMERA))) {
                        canvas.drawLine(startX, startY, startX - iPaintSize, startY, mPaint);
                        canvas.drawLine(stopX + iPaintSize, startY, stopX, startY, mPaint);
                        canvas.drawLine(startX, startY, startX, startY + iPaintSize, mPaint);
//              右下|
                        canvas.drawLine(startX, stopY - iPaintSize, startX, stopY, mPaint);
//              左下|
                        canvas.drawLine(stopX, stopY, stopX, stopY - iPaintSize, mPaint);
//              左上|
                        canvas.drawLine(stopX, startY + iPaintSize, stopX, startY, mPaint);
//              左下-
                        canvas.drawLine(stopX, stopY, stopX + iPaintSize, stopY, mPaint);
//              右下-
                        canvas.drawLine(startX - iPaintSize, stopY, startX, stopY, mPaint);
                    } else {
                        //左上-
                        canvas.drawLine(startX, startY, startX + iPaintSize, startY, mPaint);
                        //右上-
                        canvas.drawLine(stopX - iPaintSize, startY, stopX, startY, mPaint);

                        //左上|
                        canvas.drawLine(startX, startY, startX, startY + iPaintSize, mPaint);
                        //左下|
                        canvas.drawLine(startX, stopY - iPaintSize, startX, stopY, mPaint);

                        //右下|
                        canvas.drawLine(stopX, stopY, stopX, stopY - iPaintSize, mPaint);
                        //右上|
                        canvas.drawLine(stopX, startY + iPaintSize, stopX, startY, mPaint);

                        //左下-
                        canvas.drawLine(stopX, stopY, stopX - iPaintSize, stopY, mPaint);
                        //右下-
                        canvas.drawLine(startX + iPaintSize, stopY, startX, stopY, mPaint);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

}
