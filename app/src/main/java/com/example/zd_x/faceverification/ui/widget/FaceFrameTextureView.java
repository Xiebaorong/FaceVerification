package com.example.zd_x.faceverification.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.TextureView;

public class FaceFrameTextureView extends TextureView {
    private int mRatioWidth = 0;
    private int mRatioHeight = 0;
    private static final String TAG = "FaceFrameTextureView";
    public FaceFrameTextureView(Context context) {
        this(context, null);
    }

    public FaceFrameTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceFrameTextureView(Context context, AttributeSet attrs, int defStyle) {
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
}
