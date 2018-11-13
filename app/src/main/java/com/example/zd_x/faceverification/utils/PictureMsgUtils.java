package com.example.zd_x.faceverification.utils;

public class PictureMsgUtils {
    private static PictureMsgUtils instance;
    public static PictureMsgUtils getInstance(){
        if (instance ==null){
            synchronized (PictureMsgUtils.class){
                if (instance ==null){
                    instance = new PictureMsgUtils();
                }
            }
        }
        return instance;
    }

    public String getPictureImageId(){
        long time = System.currentTimeMillis();
        return "110108"+time;
    }

    public String getDeviceID(){
        return "11010801800007000001";
    }


}
