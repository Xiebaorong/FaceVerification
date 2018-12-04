package com.example.zd_x.faceverification.mvp.model;


import com.example.zd_x.faceverification.BuildConfig;

public class BaseParameterModel {
    private int version_code = BuildConfig.VERSION_CODE;
    private String version = BuildConfig.VERSION_NAME;

    public int getVersion_code() {
        return version_code;
    }

    public String getVersion() {
        return version;
    }
}
