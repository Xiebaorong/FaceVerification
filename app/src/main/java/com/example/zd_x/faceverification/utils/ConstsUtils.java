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
     * 权限
     */
    public static final int CODE_FOR_WRITE_PERMISSION = 1000;

    /**
     * 权限
     */
    public static final int NOTICE_REFRESH = 1111;
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
     * 相机编号
     */
    public static String ID_IDENTIFY = "ID";


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
     * 起始点X
     */
    public static int iStartX = 0;
    /**
     * 起始点Y
     */
    public static int iStartY = 0;
    /**
     * 剪裁宽度
     */
    public static int iWidth = 0;
    /**
     * 剪裁高度
     */
    public static int iHeight = 0;
    /**
     * SHOW_MSG 1000 展示信息
     */
    public static final int SHOW_MSG = 1000;
    /**
     * SHOW_MSG 2000 初始化成功
     */
    public static final int INIT_SUCCESS = 2000;

    /**
     * 权限判断
     */
    public static boolean isPermissions = false;



}
