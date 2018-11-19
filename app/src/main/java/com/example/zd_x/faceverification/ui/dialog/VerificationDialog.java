package com.example.zd_x.faceverification.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.base.BaseDialog;
import com.example.zd_x.faceverification.mvp.model.VerificationModel;
import com.example.zd_x.faceverification.mvp.p.IVerificationDialogPresenterCompl;
import com.example.zd_x.faceverification.mvp.view.IVerificationDialogView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerificationDialog extends BaseDialog implements IVerificationDialogView {
    private static final String TAG = "VerificationDialog";

    @BindView(R.id.tv_limit_dialog)

    TextView tvLimitDialog;
    @BindView(R.id.tv_imageId_dialog)
    TextView tvImageIdDialog;
    @BindView(R.id.tv_ErrorMsg_dialog)
    TextView tvErrorMsgDialog;
    @BindView(R.id.lv_verificationMsgList_dialog)
    ListView lvVerificationMsgListDialog;
    @BindView(R.id.bt_cancel_dialog)
    Button btCancelDialog;
    @BindView(R.id.bt_confirm_dialog)
    Button btConfirmDialog;
    private IVerificationDialogPresenterCompl dialogPresenterCompl;

    public VerificationDialog(Activity context) {
        super(context);
    }

    @Override
    protected int getDialogStyleId() {
        return R.style.CustomDialog;
    }

    @Override
    protected View getView(Activity mContext) {
        View view = View.inflate(mContext, R.layout.dialog_verification_msg_layout, null);
        ButterKnife.bind(mContext);
        dialogPresenterCompl = new IVerificationDialogPresenterCompl(this);
        return view;
    }

    @Override
    public void getVerificationMsg(VerificationModel verificationModel) {
        Log.e(TAG, "getVerificationMsg: "+verificationModel.getStatus() );
        if (verificationModel.getStatus()==0){
            lvVerificationMsgListDialog.setVisibility(View.VISIBLE);
            tvErrorMsgDialog.setVisibility(View.GONE);

        }else {
            lvVerificationMsgListDialog.setVisibility(View.GONE);
            tvErrorMsgDialog.setVisibility(View.VISIBLE);
            tvErrorMsgDialog.setText(verificationModel.getMessage());
        }
    }



    private void initEvent() {
        btConfirmDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPresenterCompl.onConfirmListener();
            }
        });
    }

}
