package com.example.zd_x.faceverification.mvp.p;

import android.content.Context;
import android.media.Image;

public interface ICameraPresenter {
    void takePicture(Context context);

    void cameraSwitch(Context context);

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
