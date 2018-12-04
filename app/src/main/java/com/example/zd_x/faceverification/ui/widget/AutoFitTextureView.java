package com.example.zd_x.faceverification.ui.widget;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.camera2.CaptureResult;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

public class AutoFitTextureView extends TextureView {
    private static final String TAG = "AutoFitTextureView";
    private Handler mHandler;
    private SurfaceView mSurfaceView02;//mSurfaceHolder02 显示框 对应SurfaceView
    private SurfaceHolder mSurfaceHolder02;
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;

    public AutoFitTextureView(Context context) {
        this(context, null);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoFitTextureView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * @param width  Relative horizontal size
     * @param height Relative vertical size
     */
    public void setAspectRatio(int width, int height) {
        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Size cannot be negative.");
        }
        mRatioWidth = width;//1080
        mRatioHeight = height;//1440
        Log.e(TAG, "setAspectRatio: " + mRatioWidth + "--" + mRatioHeight);
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);//1080
        int height = MeasureSpec.getSize(heightMeasureSpec);//1740
        Log.e(TAG, "onMeasure: " + width + "---" + height);
        if (0 == mRatioWidth || 0 == mRatioHeight) {
            Log.e(TAG, "onMeasure: 1111");
            setMeasuredDimension(width, height);
        } else {
            if (width < height * mRatioWidth / mRatioHeight) {
                Log.e(TAG, "onMeasure: 333");          //1080   //1440          //1080
                setMeasuredDimension(width, width * mRatioHeight / mRatioWidth);
            } else {
                Log.e(TAG, "onMeasure: 444");
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


}
