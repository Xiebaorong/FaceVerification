package com.example.zd_x.faceverification.utils;


import com.example.zd_x.faceverification.BuildConfig;
import com.orhanobut.logger.Logger;

public class LogUtils {

    private static final int VERBOSE = 1;
    private static final int DEBUG = 2;
    private static final int INFO = 3;
    private static final int WARN = 4;
    private static final int ERROR = 5;
    private static final int NOTHING = 6;

    private static int sLevel = BuildConfig.DEBUG ? VERBOSE : NOTHING;

    public static void v(String tag, String msg) {
        if (sLevel <= VERBOSE) {
            Logger.t(tag).v(msg);
        }
    }

    public static void d(String tag, String msg) {
        if (sLevel <= DEBUG) {
            Logger.t(tag).d(msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sLevel <= INFO) {
            Logger.t(tag).i(msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sLevel <= WARN) {
            Logger.t(tag).w(msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sLevel <= ERROR) {
            Logger.t(tag).e(msg);
        }
    }


    public static void json(String tag, String msg) {
//        if (sLevel <= ERROR) {
            Logger.t(tag).json(msg);
//        }
    }
}
