package com.example.zd_x.faceverification.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.zd_x.faceverification.BuildConfig;
import com.example.zd_x.faceverification.dao.DaoMaster;
import com.example.zd_x.faceverification.dao.DaoSession;
import com.github.yuweiguocn.library.greendao.MigrationHelper;

public class GreenDaoManager {
    private static GreenDaoManager instance;
    private static final String DATABASE_NAME = BuildConfig.APPLICATION_ID + ".db";
    private GreenDaoOpenHelper mGreenDaoOpenHelper;
    private DaoMaster mDaoMaster;
    private SQLiteDatabase db;
    private DaoSession mDaoSession;

    public static GreenDaoManager getInstance() {
        if (instance == null) {
            synchronized (GreenDaoManager.class) {
                if (instance == null) {
                    instance = new GreenDaoManager();
                }
            }
        }
        return instance;
    }


    public void initializeDatabase(Context context) {
        if (mGreenDaoOpenHelper == null) {
            //如果你想查看日志信息，请将DEBUG设置为true
            MigrationHelper.DEBUG = BuildConfig.DEBUG;
            mGreenDaoOpenHelper = new GreenDaoOpenHelper(context, DATABASE_NAME, null);
            db = mGreenDaoOpenHelper.getWritableDatabase();
            mDaoMaster = new DaoMaster(db);
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb(){
        return db;
    }
}
