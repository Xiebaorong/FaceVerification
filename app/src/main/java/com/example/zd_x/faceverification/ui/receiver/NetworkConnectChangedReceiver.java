package com.example.zd_x.faceverification.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.example.zd_x.faceverification.utils.NetworkUtils;

public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    public static final String EVENT = "event";
    public static final String ACTION = "action";
    private CheckNetworkStatusChangeListener mCheckNetworkStatusChangeListener;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int networkConnected = NetworkUtils.isNetworkConnected(context);
            mCheckNetworkStatusChangeListener.onEvent(networkConnected);

        }
    }

    public void setCheckNetworkStatusChangeListener(CheckNetworkStatusChangeListener mCheckNetworkStatusChangeListener) {
        this.mCheckNetworkStatusChangeListener = mCheckNetworkStatusChangeListener;
    }


    public interface CheckNetworkStatusChangeListener {
        void onEvent(int status);
    }
}
