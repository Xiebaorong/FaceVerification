package com.example.zd_x.faceverification.mvp.view;

public interface ICameraView {
    /**
     * 获取拍照结果
     * @param result
     */
    void getPhotoResults(int result);

    /**
     * 获取镜头切换结果
     * @param flag
     */
    void getCameraSwitch(boolean flag);

    /**
     * 显示上传进度框
     * @param flag
     */
    void showUploadDialog(boolean flag);

    void showVerificationMsgDialog(String verificationModel);
}
