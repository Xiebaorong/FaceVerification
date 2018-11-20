package com.example.zd_x.faceverification.application;

import android.app.Application;

import com.example.zd_x.faceverification.database.GreenDaoManager;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import okhttp3.OkHttpClient;


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
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder() .
                addNetworkInterceptor(new StethoInterceptor()) .build();
        initDatabase();
    }

    private void initDatabase() {
        GreenDaoManager.getInstance().initializeDatabase(this);
    }

}
