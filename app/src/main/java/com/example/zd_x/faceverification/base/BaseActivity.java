package com.example.zd_x.faceverification.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zd_x.faceverification.R;
import com.example.zd_x.faceverification.database.DataManipulation;
import com.example.zd_x.faceverification.ui.receiver.NetworkConnectChangedReceiver;
import com.example.zd_x.faceverification.utils.LogUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements NetworkConnectChangedReceiver.CheckNetworkStatusChangeListener {
    private static final String TAG = "BaseActivity";
    private ProgressDialog progressDialog;
    public Toast toast;
    private NetworkConnectChangedReceiver networkConnectChangedReceiver;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }

        ButterKnife.bind(this);
        OnActCreate(savedInstanceState);

        DataManipulation.getInstance().initDao();
        init();
        initEvent();

    }

    private void init() {
        networkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        networkConnectChangedReceiver.setCheckNetworkStatusChangeListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkConnectChangedReceiver, filter);

        //添加全局布局
//        View decorView = getWindow().getDecorView();
//        FrameLayout contentParent = decorView.findViewById(android.R.id.content);
//        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.network_hint_layout, null);
//        linearLayout = view.findViewById(R.id.ll_hint_network);
//        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
////        TextView textView = new TextView(this);
////        textView.setText("这是一个TextView\n通过DecorView将其绘制到了Activity上层");
////        textView.setGravity(Gravity.CENTER);
////        textView.setBackgroundColor(Color.RED);
//
////        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
////        contentParent.addView(textView);
//        contentParent.addView(linearLayout);
    }

    private void unRegisterReceiver() {
        if (networkConnectChangedReceiver == null) return;
        unregisterReceiver(networkConnectChangedReceiver);
        networkConnectChangedReceiver = null;
    }

    protected abstract int getLayoutId();

    protected abstract void OnActCreate(Bundle savedInstanceState);

    protected abstract void initEvent();

    protected abstract void onNetChanged(int netWorkState);

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

    @Override
    public void onEvent(int status) {
        Log.e(TAG, "onEvent: "+status );
        onNetChanged(status);
//        if (status < 0) {
//            linearLayout.setVisibility(View.VISIBLE);
//        } else {
//            linearLayout.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();
    }
}
