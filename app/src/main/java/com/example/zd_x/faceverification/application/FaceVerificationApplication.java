package com.example.zd_x.faceverification.application;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;


public class FaceVerificationApplication extends Application {
    private static FaceVerificationApplication mApplication;

    public static FaceVerificationApplication getmApplication() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;

        Logger.addLogAdapter(new AndroidLogAdapter());
    }
}
