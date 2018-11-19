package com.hanvon.faceRec;

public class FaceRect {
    public int iTop;
    public int iBottom;
    public int iLeft;
    public int iRight;

    public FaceRect() {

    }

    public FaceRect(int top, int bottom, int left, int right) {
        this.iTop = top;
        this.iBottom = bottom;
        this.iLeft = left;
        this.iRight = right;
    }


    public int getiTop() {
        return iTop;
    }

    public void setiTop(int iTop) {
        this.iTop = iTop;
    }

    public int getiBottom() {
        return iBottom;
    }

    public void setiBottom(int iBottom) {
        this.iBottom = iBottom;
    }

    public int getiLeft() {
        return iLeft;
    }

    public void setiLeft(int iLeft) {
        this.iLeft = iLeft;
    }

    public int getiRight() {
        return iRight;
    }

    public void setiRight(int iRight) {
        this.iRight = iRight;
    }


}
