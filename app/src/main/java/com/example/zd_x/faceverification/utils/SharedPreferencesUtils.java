package com.example.zd_x.faceverification.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.zd_x.faceverification.BuildConfig;
import com.example.zd_x.faceverification.R;

public class SharedPreferencesUtils {
    private static final String TAG = "SharedPreferencesUtil";
    private static SharedPreferencesUtils instance;
    private Context mContext;
    private SharedPreferences sp;
    private SharedPreferences.Editor edit;

    public static SharedPreferencesUtils getInstance() {
        if (instance == null) {
            synchronized (SharedPreferencesUtils.class) {
                if (instance == null) {
                    instance = new SharedPreferencesUtils();
                }
            }
        }
        return instance;
    }

    public void initSharedPreferences(Context context) {
        this.mContext = context;
        sp = context.getSharedPreferences(BuildConfig.APPLICATION_ID , Context.MODE_PRIVATE);
        edit = sp.edit();
    }


    /**
     * isFirst == true 为首次登录
     * @return
     */
    public boolean isFirstLogin() {
        boolean isFirst = sp.getBoolean("isF", true);
        if (isFirst) {
            SharedPreferences.Editor edit1 = sp.edit();
            edit1 = sp.edit();
            edit1.putBoolean("isF", false);
            edit1.commit();
        }
        return isFirst;
    }

    /**
     * 保存权限状态
     * @param permissionsName
     * @param isPermissions
     */
    public void savePermissions(String permissionsName, boolean isPermissions) {
        edit.putString("permissionsName", permissionsName);
        edit.putBoolean(permissionsName, isPermissions);
        edit.commit();
    }



}
