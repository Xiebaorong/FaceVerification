package com.example.zd_x.faceverification.utils;

import android.content.Context;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.application.FaceVerificationApplication;

import java.util.Arrays;
import java.util.List;

public class PictureMsgUtils {
    private static PictureMsgUtils instance;

    public static String imageID;
    public static String deviceID = "11010801800007000001";
    public static String deviceName;
    public static String faceBase64;
    public static int skip = 0;
    public static int limit = 3;
    public static String strategy = "strategy";
    public static List<String> repoIDs = Arrays.asList("75dd62c4-8eaf-4ab8-8d1f-7066e712b8ec");//list的size就固定了，不能再add
    //    public static String repoIDs = "662d1256-33d9-45b6-be5b-8cbc06960450";//list的size就固定了，不能再add
    public static int threshold = 80;

    public static PictureMsgUtils getInstance() {
        if (instance == null) {
            synchronized (PictureMsgUtils.class) {
                if (instance == null) {
                    instance = new PictureMsgUtils();
                }
            }
        }
        return instance;
    }

    public String getPictureImageId() {
        long time = System.currentTimeMillis();
        return "110108" + time + "0000000000000000000000";
    }

    public String getDeviceID() {
        return "11010801800007000001";
    }

    public String getVerificationResult(boolean flag) {
        String Result;
        if (flag) {
            Result = FaceVerificationApplication.getmApplication().getString(R.string.verificationTrue);
        } else {
            Result = FaceVerificationApplication.getmApplication().getString(R.string.verificationFalse);
        }
        return Result;
    }

}
