package com.example.zd_x.faceverification.mvp.p;

import com.example.zd_x.faceverification.ui.dialog.VerificationDialog;

public class IVerificationDialogPresenterCompl implements VerificationDialogPresenter {
    private VerificationDialog verificationDialog;

    public IVerificationDialogPresenterCompl(VerificationDialog view) {
        verificationDialog = view;
    }


    @Override
    public void onConfirmListener() {

    }
}
