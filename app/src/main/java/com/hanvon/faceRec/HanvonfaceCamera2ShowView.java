package com.hanvon.faceRec;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.hanvon.face.FaceCoreHelper;
import com.hanvon.face.HWFaceClient;

import java.nio.ByteBuffer;

import static com.hanvon.face.HWCoreHelper.FACE_HANDLER;

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
                Image image = null;
                try {
                    image = reader.acquireNextImage();
                    if (image == null) {
                        return;
                    }
                    ByteBuffer ybuffer = image.getPlanes()[0].getBuffer();
                    final byte[] ydata = new byte[ybuffer.remaining()];
                    ybuffer.get(ydata);

                    new Thread(new cameraPreview(ydata, mSurfaceHolder02)).start();
                } catch (Exception e) {
                    Log.e(TAG, "onImageAvailable:----- " + e.getMessage(), e);
                } finally {
                    if (image != null) {
                        image.close();
                    }
                }

            }

        }
    };

    private class cameraPreview implements Runnable {
        byte[] data;
        SurfaceHolder mSurfaceHolder02;

        public cameraPreview(byte[] ydata, SurfaceHolder mSurfaceHolder) {
            data = ydata;
            mSurfaceHolder02 = mSurfaceHolder;
        }

        @Override
        public void run() {
            try {
                int[] faceNum = new int[1];
                int intFaceNum = 1;
                faceNum[0] = intFaceNum;    // 单人脸
                int width = Camera2Helper.PIXEL_WIDTH;
                int height = Camera2Helper.PIXEL_HEIGHT;

                int[] pFacePos = new int[(HWFaceClient.HW_FACEPOS_LEN) * intFaceNum];
                float[] pEyePos = new float[(HWFaceClient.HW_EYEPOS_LEN) * intFaceNum];

                final byte[] rotateData = new byte[data.length];
                if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
                    width = Camera2Helper.PIXEL_HEIGHT;
                    height = Camera2Helper.PIXEL_WIDTH;

                    UtilFunc.rotateYuvData(rotateData, data, Camera2Helper.PIXEL_WIDTH, Camera2Helper.PIXEL_HEIGHT, 1);
                } else if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.REAR_CAMERA))) {
                    width = Camera2Helper.PIXEL_HEIGHT;
                    height = Camera2Helper.PIXEL_WIDTH;
                    UtilFunc.rotateYuvData(rotateData, data, Camera2Helper.PIXEL_WIDTH, Camera2Helper.PIXEL_HEIGHT, 0);
                }
                int result = FaceCoreHelper.HWFaceDetectFaces(FACE_HANDLER, rotateData, width, height, pFacePos, pEyePos, faceNum);
                Log.e(TAG, "run: "+result );
                Canvas canvas = mSurfaceHolder02.lockCanvas(null);
                if (canvas == null) {
                    return;
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                if (pFacePos[0] > 0 && result == 0) {
                    canvasDarwLine(canvas, pFacePos, width, height);
                }
                if (canvas != null) {
                    mSurfaceHolder02.unlockCanvasAndPost(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "run: "+e );
            }
        }
    }

    private void canvasDarwLine(Canvas canvas, int[] pFacePos, int iWidth, int iHeight) {
        try {
            Paint mPaint = new Paint();
            mPaint.setColor(0xff00FFFF);// 画笔为绿色
            mPaint.setStrokeWidth(2);// 设置画笔粗细
            mPaint.setTextSize(16.0f);
            if (pFacePos[0] > 0) {
                int startX = pFacePos[2];
                int startY = pFacePos[0];
                int stopX = pFacePos[3];
                int stopY = pFacePos[1];


                //镜像
                if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
                    startX = iWidth - startX;
                    stopX = iWidth - stopX;
                }
//                else {
//                    //TODO
//                    startX = iWidth - startX;
//                    stopX = iWidth - stopX;
//                }

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



                if (ConstsUtils.CAMERA_ID.equals(String.valueOf(ConstsUtils.FRONT_CAMERA))) {
                    float newHratio = (float) Camera2Helper.PIXEL_WIDTH / (float) heightf;
                    float newWratio = (float) Camera2Helper.PIXEL_HEIGHT / (float) widthf;
                    ConstsUtils.iLeftTop = (int) ((1080 - startX) * newWratio);
                    ConstsUtils.iLeftBottom = (int) (startY * newHratio);
                    ConstsUtils.iRightTop = (int) (((1080 - stopX) * newWratio) - ConstsUtils.iLeftTop);
                    ConstsUtils.iRightBottom = (int) ((stopY * newHratio)-ConstsUtils.iLeftBottom);


                    canvas.drawLine(startX, startY, startX - iPaintSize, startY, mPaint);
                    canvas.drawLine(stopX + iPaintSize, startY, stopX, startY, mPaint);

                    canvas.drawLine(startX, startY, startX, startY + iPaintSize, mPaint);
                    canvas.drawLine(startX, stopY - iPaintSize, startX, stopY, mPaint);

                    canvas.drawLine(stopX, stopY, stopX, stopY - iPaintSize, mPaint);
                    canvas.drawLine(stopX, startY + iPaintSize, stopX, startY, mPaint);

                    canvas.drawLine(stopX, stopY, stopX + iPaintSize, stopY, mPaint);
                    canvas.drawLine(startX - iPaintSize, stopY, startX, stopY, mPaint);
                } else {
                    // TODO: 2018/11/16
                    canvas.drawLine(startX, startY, startX - iPaintSize, startY, mPaint);
                    canvas.drawLine(stopX + iPaintSize, startY, stopX, startY, mPaint);

                    canvas.drawLine(startX, startY, startX, startY + iPaintSize, mPaint);
                    canvas.drawLine(startX, stopY - iPaintSize, startX, stopY, mPaint);

                    canvas.drawLine(stopX, stopY, stopX, stopY - iPaintSize, mPaint);
                    canvas.drawLine(stopX, startY + iPaintSize, stopX, startY, mPaint);

                    canvas.drawLine(stopX, stopY, stopX + iPaintSize, stopY, mPaint);
                    canvas.drawLine(startX - iPaintSize, stopY, startX, stopY, mPaint);
                }
            }

        } catch (Exception e) {
            Log.d(TAG, e.getMessage(),e);
        }

    }

}
