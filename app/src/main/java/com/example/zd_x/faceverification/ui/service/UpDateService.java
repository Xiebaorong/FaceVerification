package com.example.zd_x.faceverification.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class UpDateService extends Service {
    @Override
    public void onCreate(){
        update();
    }

    private void update() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
