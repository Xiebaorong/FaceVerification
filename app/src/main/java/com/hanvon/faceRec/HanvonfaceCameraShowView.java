package com.hanvon.faceRec;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class HanvonfaceCameraShowView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = "HanvonfaceCameraShowVie";
    public static HanvonfaceCameraShowView hanvonfaceShowView = null;
    private Handler mHandler = null;
    private SurfaceView mSurfaceView02;//mSurfaceHolder02 显示框 对应SurfaceView
    private SurfaceHolder mSurfaceHolder, mSurfaceHolder02;// mSurfaceHolder 相机Preview，mSurfaceHolder02 显示框
    private volatile boolean isCanDetectFace = true;


    public HanvonfaceCameraShowView(Context context) {
        super(context);
        hanvonfaceShowView = this;
    }

    public HanvonfaceCameraShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        hanvonfaceShowView = this;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        CameraHelper.cameraHelper.startPreview(holder, this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 设置显示和绘制SurfaceView
     *
     * @param view02 绘制 Surface
     */
    public void setSurfaceView(SurfaceView view02, Handler handler) {
        /* 以SurfaceView作为相机Preview之用mSurfaceView01显示相机mSurfaceView02显示脸框  */
        this.mHandler = handler;
        mSurfaceView02 = view02;
        mSurfaceView02.setZOrderOnTop(true);
        getSurfaceHolder();
    }

    public void getSurfaceHolder() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceHolder02 = mSurfaceView02.getHolder();
        mSurfaceHolder02.setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        synchronized (this) {
            Canvas canvas = mSurfaceHolder02.lockCanvas(null);
            try {
                if (null != canvas) {
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    if (isCanDetectFace) {
                        //人脸识别
                        HWFaceIDCardCompareLib.getInstance().SetCameraPreviewFrame(data, CameraHelper.PIXEL_WIDTH, CameraHelper.PIXEL_HEIGHT, mHandler);
                        canvasDarwLine(canvas, HWFaceIDCardCompareLib.getInstance().iWidth, HWFaceIDCardCompareLib.getInstance().iHeight);
                    }
                }
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            } finally {
                if (null != canvas) {
                    mSurfaceHolder02.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void canvasDarwLine(Canvas canvas, int iWidth, int iHeight) {
        try {
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
                if (CameraHelper.CAMERA_FACING == Camera.CameraInfo.CAMERA_FACING_BACK) {
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


                if (CameraHelper.CAMERA_FACING == Camera.CameraInfo.CAMERA_FACING_BACK) {
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
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }


}
