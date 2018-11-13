package com.hanvon.faceRec;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;
import android.view.TextureView;

import com.example.zd_x.faceverification.application.FaceVerificationApplication;
import com.example.zd_x.faceverification.callBack.PictureCallBack;
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

    }


    private final ImageReader.OnImageAvailableListener onImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Log.e(TAG, "onImageAvailable: "+Camera2Helper.isTakePicture );
            if (Camera2Helper.isTakePicture) {
                FileUtils.imageSaver(reader.acquireNextImage());
            } else {
                Image image = reader.acquireNextImage();
                image.close();
            }

        }
    };


}
