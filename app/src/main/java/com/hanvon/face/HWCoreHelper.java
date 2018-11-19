package com.hanvon.face;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.zd_x.faceverification.utils.ConstsUtils;
import com.hanvon.faceRec.Consts;
import com.hanvon.faceRec.HWConsts;


/**
 * Created by lianxi on 2017/10/20.
 */

public class HWCoreHelper {
    private static final String TAG = "HWCoreHelper";
    private static String IP = "182.92.162.37";
    private static int port = 8888;
    public static byte[] CARD_HANDLER = new byte[4];
    public static byte[] FACE_HANDLER = new byte[4];

    public static void InitFaceClient(final Context context, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int Result = HWFaceClient.InitFaceClient(IP, port, context);
                Log.e(TAG, "InitFaceClient"+Result );
                if (Result == -1) {
                    handler.sendMessage(handler.obtainMessage(ConstsUtils.SHOW_MSG, "服务器连接失败"));
                } else {
                    handler.sendMessage(handler.obtainMessage(ConstsUtils.SHOW_MSG, "服务器连接成功"));
                    Log.e(TAG, "HWCoreHelper: 服务器连接成功" );
                }
            }
        }).start();
    }

    public static void initHWCore(Context context, Handler handler) {
        initFaceCore(context , handler);
    }


    private static void initFaceCore(Context context, Handler handler) {
        if (HWFaceClient.GetKeyCode() == HWFaceClient.HW_OK) {
            if (HWFaceClient.bpKeyCode != null) {
                //初始化核心
                int initResult = FaceCoreHelper.HWFaceInitial(FACE_HANDLER, HWFaceClient.bpKeyCode, context);
                if (initResult == HWFaceClient.HW_OK) {
                    handler.sendMessage(handler.obtainMessage(Consts.SHOW_MSG, "初始化成功"));
                    Log.e(TAG, "1764033-initFaceCore:初始化成功" );
                    int[] featureSize = new int[1];
                    FaceCoreHelper.HWFaceGetFeatureSize(FACE_HANDLER, featureSize);
                    HWConsts.iFeatureSize = featureSize[0];
                } else {
                    Log.d(TAG, "initFaceCore====== " + initResult);
                }
            }
        }else {
            InitFaceClient(context, handler);
        }
    }

    public static void releaseCore() {
        FaceCoreHelper.HWFaceRelease(FACE_HANDLER);
        HWFaceClient.ReleaseFaceClient();
    }
}
