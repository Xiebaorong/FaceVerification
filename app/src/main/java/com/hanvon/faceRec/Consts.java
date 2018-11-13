package com.hanvon.faceRec;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lianxi on 2017/10/20.
 */

public class Consts {
    /**
     * SEND_CARD_DATA 发送读卡数据
     */
    public static final int SEND_CARD_DATA = 100;
    /**
     * READ_CARD_FAIL 读卡失败
     */
    public static final int READ_CARD_FAIL = 102;
    /**
     * SHOW_MSG 1000 展示信息
     */
    public static final int SHOW_MSG = 1000;
    /**
     * SHOW_MSG 1000 初始化成功
     */
    public static final int INIT_SUCCESS = 2000;
    /**
     * 显示比对结果
     */
    public static final int SHOW_RESULT = 1001;

    /**
     * 显示抓取结果
     */
    public static final int SHOW_CAPTURE_FACE_RESULT = 3001;

    /**
     * 重置读卡信息
     */
    public static final int RESET_READ_CARD = 3002;

    /**
     * 开始人脸比对
     */
    public static final int START_FACE_COMPARE = 1002;

    public static String CARD_ID = "身份证号: ";
    public static String CARD_NAME = "姓名: ";
    public static String COMPARE_TIME = "校验时间: ";
    protected static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static String getCompareTime() {
        return df.format(new Date());
    }
}
