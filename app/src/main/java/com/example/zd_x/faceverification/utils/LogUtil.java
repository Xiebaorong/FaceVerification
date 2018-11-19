package com.example.zd_x.faceverification.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {

    private static final String TAG = "LogUtil";
    //Log开关
    private static boolean open = true;
    private static final String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "LogServer" + File.separator;
    public static String fileName = null;
    private static boolean isAppend = false;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final boolean DEBUG = true;

    public static void d(String message) {
        if (DEBUG) {
            String logStr = createMessage(message);
            writeLog2SDCard("d", logStr);
            Log.d(TAG, logStr);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            String logStr = createMessage(message);
            writeLog2SDCard("e", logStr);
            Log.e(TAG, logStr);
        }
    }

    public static void i(String message) {
        if (DEBUG) {
            String logStr = createMessage(message);
            writeLog2SDCard("i", logStr);
            Log.i(TAG, logStr);
        }
    }

    public static void v(String message) {
        if (DEBUG) {
            String logStr = createMessage(message);
            writeLog2SDCard("v", logStr);
            Log.v(TAG, logStr);
        }
    }

    public static void w(String message) {
        if (DEBUG) {
            String logStr = createMessage(message);
            writeLog2SDCard("w", logStr);
            Log.w(TAG, logStr);
        }
    }

    public static void wtf(String message) {
        if (DEBUG) {
            String logStr = createMessage(message);
            writeLog2SDCard("wtf", logStr);
            Log.wtf(TAG, logStr);
        }
    }

    public static void allLog(String message) {
        if (DEBUG) {
            String logStr = createMessage(message);
            writeLog2SDCard("all", logStr);
            LogUtil.all(TAG, logStr);
        }
    }

    /**
     * 输出全部log
     */
    private static void all(String TAG, String msg) {
        int strLength = msg.length();
        int start = 0;
        int end = 2000;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.e(TAG + i, msg.substring(start, end));
                start = end;
                end = end + 2000;
            } else {
                Log.e(TAG, msg.substring(start, strLength));
                break;
            }
        }
    }

    public static void println(String message) {
        if (DEBUG) {
            String logStr = createMessage(message);
            writeLog2SDCard("println", logStr);
            Log.println(Log.INFO, TAG, logStr);
        }
    }

    /**
     * 获取有类名与方法名的logString
     *
     * @param rawMessage
     * @return
     */
    private static String createMessage(String rawMessage) {
        /**
         * Throwable().getStackTrace()获取的是程序运行的堆栈信息，也就是程序运行到此处的流程，以及所有方法的信息
         * 这里我们为什么取2呢？0是代表createMessage方法信息，1是代表上一层方法的信息，这里我们
         * 取它是上两层方法的信息
         */
        StackTraceElement stackTraceElement = new Throwable().getStackTrace()[2];
        String fullClassName = stackTraceElement.getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        return className + "." + stackTraceElement.getMethodName() + "(): " + rawMessage;
    }


    /**
     * 程序每次运行创建一个新的txt，以时间为文件名
     *
     * @param type    log的类型
     * @param context log的内容
     */
    private static void writeLog2SDCard(String type, String context) {
        if (fileName == null)
            fileName = sdf.format(new Date(System.currentTimeMillis())) + ".txt";
        File file = new File(basePath + fileName);
        String dateStr = sdf.format(new Date(System.currentTimeMillis()));
        context = dateStr + "==" + type + "==" + context;

        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend)));
            bufferedWriter.write(context);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            if (!isAppend) {
                isAppend = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeLog(String context) {
        if (!open) return;
        if (fileName == null)
            fileName = sdf.format(new Date(System.currentTimeMillis())) + ".txt";
        File file = new File(basePath + fileName);
        String dateStr = sdf.format(new Date(System.currentTimeMillis()));
        context = dateStr + "==" + context;
//        Log.e(TAG,  "fileName:"+file.getAbsolutePath());
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend)));
            bufferedWriter.write(context);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            if (!isAppend) {
                isAppend = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
