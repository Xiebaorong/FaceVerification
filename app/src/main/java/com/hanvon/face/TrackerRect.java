package com.hanvon.face;

/**
 * Created by Administrator on 2018/4/20.
 */

public class TrackerRect {

    public int x;
    public int y;
    public int width;
    public int height;
    public float pfInfoConfidence;

    public TrackerRect(){}

    public TrackerRect(int top, int bottom, int left, int right)
    {
        this.x = left;
        this.y = top;
        this.width = right - left;
        this.height = bottom - top;
    }
}
