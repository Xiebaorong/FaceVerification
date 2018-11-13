package com.example.zd_x.faceverification.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

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
}
