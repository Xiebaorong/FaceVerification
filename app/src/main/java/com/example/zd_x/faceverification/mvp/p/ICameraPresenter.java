package com.example.zd_x.faceverification.mvp.p;

import android.content.Context;

public interface ICameraPresenter {
    void takePicture(Context context);

    void cameraSwitch();
    /**
     * 人脸对比
     */
    void requestContrast(Context context);
}
