package com.example.zd_x.faceverification.mvp.p.compl;

import com.example.zd_x.faceverification.mvp.p.IVerificationDialogPresenter;
import com.example.zd_x.faceverification.ui.dialog.VerificationDialog;

public class VerificationDialogPresenterCompl implements IVerificationDialogPresenter {
    private VerificationDialog verificationDialog;

    public VerificationDialogPresenterCompl(VerificationDialog view) {
        verificationDialog = view;
    }


    @Override
    public void onConfirmListener() {

    }
}
