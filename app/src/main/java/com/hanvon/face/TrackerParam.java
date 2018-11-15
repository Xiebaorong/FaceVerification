package com.hanvon.face;

/**
 * Created by Administrator on 2018/4/19.
 */

public class TrackerParam {
    // [输入]最低置信度，当执行跟踪后，会过滤掉低于此置信度的TrackInfo，推荐值：0.87，请根据使用环境设定
    public float min_confidence;
    // [输入]跟踪框融合阀值，目前尚未使用此字段，推荐值：0.5
    public float min_bBoxMergeRate;
    // [输入]视频源帧率，即一秒钟获取到的帧数，设定的数值与实际的帧率不符会造成跟踪器严重掉框
    public float fps;
    // [输入]跟踪器内部线程数，HW_UpdateTracker函数支持多线程处理。参考，0：调用方线程（单线程）执行。1：新线程（单线程）执行。2：新线程（2线程）执行
    public int thread;
}
