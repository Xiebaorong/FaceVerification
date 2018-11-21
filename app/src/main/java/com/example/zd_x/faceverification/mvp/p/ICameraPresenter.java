package com.example.zd_x.faceverification.mvp.p;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.os.Handler;

public interface ICameraPresenter {
    /**
     * 拍照
     * @param context
     */
    void takePicture(Context context, Handler handler);

    /**
     * 相机切换
     * @param context
     */
    void cameraSwitch(Activity context);

    /**
     * 人脸对比
     */
    void requestContrast(Context context );

    /**
     * 重启预览
     * @param context
     */
    void restartPreview(Context context);
}
