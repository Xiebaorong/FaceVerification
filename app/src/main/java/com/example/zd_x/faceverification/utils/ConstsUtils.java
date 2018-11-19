package com.example.zd_x.faceverification.utils;

import okhttp3.MediaType;

public class ConstsUtils {
    /**
     * 成功
     */
    public final static int OK = 0;

    /**
     * 失败
     */
    public final static int FAIL = -1;

    /**
     * 编码成功
     */
    public final static int SUCCEED = 1;

    /**
     * 后置相机
     */
    public final static int REAR_CAMERA = 0;

    /**
     * 前置相机
     */
    public final static int FRONT_CAMERA = 1;

    /**
     * USB相机
     */
    public final static int USB_CAMERA = 2;

    /**
     * 相机编号
     */
    public static String CAMERA_ID = "0";

    /**
     * 显示Dialog
     */
    public final static boolean SHOW_DIALOG = true;

    /**
     * 取消Dialog
     */
    public final static boolean DIS_DIALOG = false;

    /**
     * JSON
     */
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 人脸左上点
     */
    public static int iLeftTop = 0;
    /**
     * 左下
     */
    public static int iLeftBottom = 0;
    /**
     * 右上
     */
    public static int iRightTop = 0;
    /**
     * 右下
     */
    public static int iRightBottom = 0;
}
