package com.example.zd_x.faceverification.mvp.view;

public interface ICameraView {
    void getPhotoResults(int result);

    void getCameraSwitch(boolean flag);

    void showUploadDialog(boolean flag);

    void showVerificationMsgDialog(String verificationModel);
}
