package com.example.zd_x.faceverification.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.zd_x.faceverification.database.DataManipulation;
import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    public Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        ButterKnife.bind(this);
        OnActCreate(savedInstanceState);

        initEvent();

        DataManipulation.getInstance().initDao();
    }

    protected abstract int getLayoutId();

    protected abstract void OnActCreate(Bundle savedInstanceState);

    protected abstract void initEvent();


    public void showToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(this, content, toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();

    }

    public void showProgressDialog() {
        showProgressDialog("努力提交中...");
    }

    /**
     * 取消对话框显示
     */
    public void disMissDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showProgressDialog(String msg) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(msg);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(true);
        try {
            progressDialog.show();
        } catch (WindowManager.BadTokenException exception) {
            exception.printStackTrace();
        }
    }
}
