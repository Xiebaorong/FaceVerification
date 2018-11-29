package com.example.zd_x.faceverification.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.params.Face;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.hanvon.faceRec.Camera2Helper;

public class CameraPreviewView extends TextureView implements TextureView.SurfaceTextureListener {
    private static final String TAG = "CameraPreviewView";
    public static CameraPreviewView cameraPreviewView = null;
    private Handler mHandler;
    private SurfaceView mSurfaceView02;//mSurfaceHolder02 显示框 对应SurfaceView
    private SurfaceHolder mSurfaceHolder02;
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public CameraPreviewView(Context context) {
        super(context,null);
        cameraPreviewView = this;
    }

    public CameraPreviewView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
        cameraPreviewView = this;
        setSurfaceTextureListener(this);

    }

    public CameraPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        cameraPreviewView = this;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
        Log.e(TAG, "onSurfaceTextureAvailable: " + width + "---h" + height + "----" + this.getTextureView().getWidth() + "---" + this.getTextureView().getHeight());
        Camera2Helper.camera2Helper.openCamera((Activity) getContext(), this);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

    }

    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;
        mRatioHeight = height;
        requestLayout();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                setMeasuredDimension(height * mRatioWidth / mRatioHeight, height);
            }
        }

    }

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

    public TextureView getTextureView() {
        return this;
    }

    public void showFaceView(final CaptureResult request, final Size cPixelSize) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    Face[] faces = request.get(CaptureResult.STATISTICS_FACES);
                    Canvas canvas = mSurfaceHolder02.lockCanvas(null);
                    if (canvas == null) {
                        return;
                    }
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    if (faces.length > 0) {
                        Rect rect = faces[0].getBounds();
                        canvasDarwLine(canvas, rect, cPixelSize);
                    }
                    if (canvas != null) {
                        mSurfaceHolder02.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }).start();

    }


    private void canvasDarwLine(Canvas canvas, Rect rect, Size cPixelSize) {
        Paint p = new Paint();
        p.setColor(0xff00FFFF);// 画笔为绿色
        p.setStrokeWidth(4);// 设置画笔粗细
        p.setStyle(Paint.Style.STROKE);//使绘制的矩形中空
        int startX = rect.top;//  t  3
        int startY = rect.left;// l  4
        int stopX = rect.bottom;//b  1
        int stopY = rect.right;// r  2
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        float scaleWidth = height * 1.0f / cPixelSize.getWidth();
        float scaleHeight = width * 1.0f / cPixelSize.getHeight();
        //添加 比例换算
        startX = (int) ((float) (startX) * scaleWidth);
        startY = (int) ((float) startY * scaleHeight);
        stopX = (int) ((float) (stopX * scaleWidth));
        stopY = (int) ((float) (stopY * scaleHeight));
//        Log.e(TAG, "[T" + 0 + "]:[startX:" + startX + ",startY:" + startY + ",stopX:" + stopX + ",stopY:" + stopY + "]");
        if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
            stopX = width - stopX;
            stopY = height - stopY;
            startX = width - startX;
            startY = height - startY;
            canvas.drawRect(stopX, stopY, startX, startY, p);// 长方形
//            int iPaintSize = (stopY - startY) / 6;
//
//            canvas.drawRect(stopX, stopY, startX, startY, p);
//            //左上-
//            canvas.drawLine(stopX + iPaintSize, stopY, stopX, stopY, p);
//            canvas.drawLine(stopX + iPaintSize , stopY, stopX + iPaintSize , stopY - iPaintSize, p);
//
//            //右上-
//            canvas.drawLine(startX, stopY, startX + iPaintSize, stopY, p);
//            canvas.drawLine(startX, stopY, startX, stopY - iPaintSize, p);
//
//            //左下-
//            canvas.drawLine(stopX + iPaintSize, startY, stopX , startY, p);
//            canvas.drawLine(stopX + iPaintSize, startY, stopX + iPaintSize, startY + iPaintSize, p);
//
//            //右下-
//            canvas.drawLine(startX, startY, startX + iPaintSize, startY, p);
//            canvas.drawLine(startX, startY, startX, startY + iPaintSize, p);
        } else {
//            canvas.drawRect(canvas.getWidth()-b, canvas.getHeight()-r, canvas.getWidth()-t, canvas.getHeight()-l,getPaint());
            stopX = width - stopX;
            startX = width - startX;
            canvas.drawRect(stopX,startY,startX,stopY,p);
//            int iPaintSize = (stopY - startY) / 6;
//            //左上-
//            canvas.drawLine(stopX, startY, stopX - iPaintSize, startY, p);
//            canvas.drawLine(stopX - iPaintSize, startY, stopX - iPaintSize, startY + iPaintSize, p);
//
//            //左下-
//            canvas.drawLine(stopX, stopY, stopX - iPaintSize, stopY, p);
//            canvas.drawLine(stopX - iPaintSize, stopY, stopX - iPaintSize, stopY - iPaintSize, p);
//
//            //右上-
//            canvas.drawLine(startX - iPaintSize, startY, startX, startY, p);
//            canvas.drawLine(startX, startY, startX, startY + iPaintSize, p);
//
//            //右下-
//            canvas.drawLine(startX - iPaintSize, stopY, startX, stopY, p);
//            canvas.drawLine(startX, stopY, startX, stopY - iPaintSize, p);


        }

    }


}
