package com.example.zd_x.faceverification.http;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.zd_x.faceverification.callBack.LoadCallBack;
import com.vector.update_app.HttpManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateAppHttpUtil implements HttpManager {

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull Callback callBack) {
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        OkHttpManager.getInstance().postRequest(url, params, new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onResponse(response.message());
            }
        });
    }

    @Override
    public void download(@NonNull String url, @NonNull final String path, @NonNull final String fileName, @NonNull final FileCallback callback) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            OkHttpManager.getInstance().postRequest(url, null, new okhttp3.Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    InputStream is = null;//输入流
                    FileOutputStream fos = null;//输出流
                    try {
                        is = response.body().byteStream();//获取输入流
                        File file = null;
                        long total = response.body().contentLength();//获取文件大小
                        if (is != null) {
                            Log.d("SettingPresenter", "onResponse: 不为空");
                            file = new File(path, fileName);// 设置路径
                            fos = new FileOutputStream(file);
                            byte[] buf = new byte[1024];
                            int ch = -1;
                            int process = 0;
                            while ((ch = is.read(buf)) != -1) {
                                fos.write(buf, 0, ch);
                                process += ch;
                                callback.onProgress(process, total);//这里就是关键的实时更新进度了！
                            }
                        }
                        fos.flush();
                        callback.onResponse(file);
                        if (fos != null) {
                            fos.close();
                        }
                    } finally {
                        try {
                            if (is != null) is.close();
                        } catch (IOException e) {
                        }
                        try {
                            if (fos != null) fos.close();
                        } catch (IOException e) {
                        }
                    }
                }
            });
        }

    }
}
