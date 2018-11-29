package com.hanvon.faceRec;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.example.zd_x.faceverification.utils.LogUtil;
import com.hanvon.face.FaceCoreHelper;
import com.hanvon.face.HWFaceClient;

import java.nio.ByteBuffer;

import static com.hanvon.face.HWCoreHelper.FACE_HANDLER;

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
        try {
            startListener();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e + "");
        }

    }

    public HanvonfaceCamera2ShowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        hanvonfaceShowView = this;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Camera2Helper.camera2Helper.openCamera((Activity) getContext(), this);
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        Log.e(TAG, "onSurfaceTextureSizeChanged: 22222");
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.e(TAG, "onSurfaceTextureSizeChanged: 33333");
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public boolean getAvailable() {
//        if (this.isAvailable()) {
//            Camera2Helper.camera2Helper.takePreview(this, onImageAvailableListener);
//        }

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

    public TextureView getTextureView() {
        return this;
    }

    private Canvas canvas;

    public void cameraPreview(final Image image) {
        int[] faceNum = new int[1];
        int intFaceNum = 1;
        faceNum[0] = intFaceNum;    // 单人脸
        int width = Camera2Helper.PIXEL_WIDTH;
        int height = Camera2Helper.PIXEL_HEIGHT;

        final int[] pFacePos = new int[(HWFaceClient.HW_FACEPOS_LEN) * intFaceNum];
        final float[] pEyePos = new float[(HWFaceClient.HW_EYEPOS_LEN) * intFaceNum];

        ByteBuffer ybuffer = image.getPlanes()[0].getBuffer();
        final byte[] ydata = new byte[ybuffer.remaining()];
        ybuffer.get(ydata);

        final byte[] rotateData = new byte[ydata.length];

        if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
            width = Camera2Helper.PIXEL_HEIGHT;
            height = Camera2Helper.PIXEL_WIDTH;
            UtilFunc.rotateYuvData(rotateData, ydata, Camera2Helper.PIXEL_WIDTH, Camera2Helper.PIXEL_HEIGHT, 1);
        } else if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.REAR_CAMERA))) {
            width = Camera2Helper.PIXEL_HEIGHT;
            height = Camera2Helper.PIXEL_WIDTH;
            UtilFunc.rotateYuvData(rotateData, ydata, Camera2Helper.PIXEL_WIDTH, Camera2Helper.PIXEL_HEIGHT, 0);
        }
        image.close();
        final int result = FaceCoreHelper.HWFaceDetectFaces(FACE_HANDLER, rotateData, width, height, pFacePos, pEyePos, faceNum);
        if (result == ConstsUtils.OK) {
            mHandler.sendMessage(mHandler.obtainMessage(ConstsUtils.WHAT_SHOW_MSG, "人脸检测成功"));
        } else if (ConstsUtils.iStartX == 0) {
            mHandler.sendMessage(mHandler.obtainMessage(ConstsUtils.WHAT_SHOW_MSG, "未检测到人脸"));
        }
        final int finalWidth = width;
        final int finalHeight = height;
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    canvas = mSurfaceHolder02.lockCanvas(null);
                    try {
                        if (canvas == null) {
                            return;
                        }

                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                        canvasDarwLine(canvas, pFacePos, finalWidth, finalHeight);
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.e("run: " + e);
                    } finally {
                        if (canvas != null) {
                            mSurfaceHolder02.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }).start();
    }

    private void canvasDarwLine(Canvas mCanvas, int[] mPFacePos, int width, int height) {
        if (mPFacePos[0] > 0) {
            Paint mPaint = new Paint();
            mPaint.setColor(0xff00FFFF);// 画笔为绿色
            mPaint.setStrokeWidth(2);// 设置画笔粗细
            mPaint.setTextSize(16.0f);
            int startX = mPFacePos[2];
            int startY = mPFacePos[0];
            int stopX = mPFacePos[3];
            int stopY = mPFacePos[1];
            //镜像
            if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
                startX = width - startX;
                stopX = width - stopX;
            }
            int heightf = mSurfaceHolder02.getSurfaceFrame().height();
            int widthf = mSurfaceHolder02.getSurfaceFrame().width();
            float hratio = (float) heightf / (float) height;
            float wratio = (float) widthf / (float) width;
            //剪裁使用
            float newHratio = (float) Camera2Helper.PIXEL_WIDTH / (float) heightf;
            float newWratio = (float) Camera2Helper.PIXEL_HEIGHT / (float) widthf;

            //添加 比例换算
            startX = (int) ((float) startX * wratio);
            startY = (int) ((float) startY * hratio);
            stopX = (int) ((float) stopX * wratio);
            stopY = (int) ((float) stopY * hratio);
            int iPaintSize = (stopY - startY) / 6;

            if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
                //剪裁
                ConstsUtils.iStartX = (int) ((1080 - startX) * newWratio);
                ConstsUtils.iStartY = (int) (startY * newHratio);
                ConstsUtils.iWidth = (int) (((1080 - stopX) * newWratio) - ConstsUtils.iStartX);
                ConstsUtils.iHeight = (int) ((stopY * newHratio) - ConstsUtils.iStartY);
                //画框
                mCanvas.drawLine(startX, startY, startX - iPaintSize, startY, mPaint);
                mCanvas.drawLine(stopX + iPaintSize, startY, stopX, startY, mPaint);

                mCanvas.drawLine(startX, startY, startX, startY + iPaintSize, mPaint);
                mCanvas.drawLine(startX, stopY - iPaintSize, startX, stopY, mPaint);

                mCanvas.drawLine(stopX, stopY, stopX, stopY - iPaintSize, mPaint);
                mCanvas.drawLine(stopX, startY + iPaintSize, stopX, startY, mPaint);

                mCanvas.drawLine(stopX, stopY, stopX + iPaintSize, stopY, mPaint);
                mCanvas.drawLine(startX - iPaintSize, stopY, startX, stopY, mPaint);
            } else {
                //剪裁
                ConstsUtils.iStartX = (int) (startX * newWratio);
                ConstsUtils.iStartY = (int) (startY * newHratio);
                ConstsUtils.iWidth = (int) ((stopX * newWratio) - ConstsUtils.iStartX);
                ConstsUtils.iHeight = (int) ((stopY * newHratio) - ConstsUtils.iStartY);

                //左上-
                mCanvas.drawLine(startX, startY, startX + iPaintSize, startY, mPaint);
                //右上-
                mCanvas.drawLine(stopX - iPaintSize, startY, stopX, startY, mPaint);

                //左上|
                mCanvas.drawLine(startX, startY, startX, startY + iPaintSize, mPaint);
                //左下|
                mCanvas.drawLine(startX, stopY - iPaintSize, startX, stopY, mPaint);

                //右下|
                mCanvas.drawLine(stopX, stopY, stopX, stopY - iPaintSize, mPaint);
                //右上|
                mCanvas.drawLine(stopX, startY + iPaintSize, stopX, startY, mPaint);

                //左下-
                mCanvas.drawLine(stopX, stopY, stopX - iPaintSize, stopY, mPaint);
                //右下-
                mCanvas.drawLine(startX + iPaintSize, stopY, startX, stopY, mPaint);
            }
        } else {
            ConstsUtils.iStartX = 0;
            ConstsUtils.iStartY = 0;
        }

    }
}
