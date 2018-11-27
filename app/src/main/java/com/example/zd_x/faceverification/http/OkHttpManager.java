package com.example.zd_x.faceverification.http;

import com.example.zd_x.faceverification.callBack.BaseCallBack;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.ByteString;

public class OkHttpManager {
    private static final String TAG = "OkHttpManager";
    private static final long DEFAULT_DIR_CACHE = 50 * 1024 * 1024;
    private static OkHttpManager instance;
    private OkHttpClient mOkHttpClient;
    private Gson mGson;

    public static OkHttpManager getInstance() {
        if (instance == null) {
            synchronized (OkHttpManager.class) {
                instance = new OkHttpManager();
            }
        }
        return instance;
    }

    public OkHttpManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//连接失败后是否重新连接
                .connectTimeout(10, TimeUnit.SECONDS)//超时时间15S
//                 .addInterceptor(new NetInterceptorUtil())
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
//                .cache(cache)//缓存器
                .build();
        mGson = new Gson();
    }

    public void get(String url, BaseCallBack callBack) {
        Request request = buildRequest(url, (Map<String, String>) null, HttpMethodType.GET);
        doRequest(request, callBack);
    }


    public void postRequest(String url, Map<String, String> params, BaseCallBack callBack) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        doRequest(request, callBack);
    }

    public void postRequest(String url, Map<String, String> params, okhttp3.Callback callback) {
        Request request = buildRequest(url, params, HttpMethodType.POST);
        doRequest(request,callback);
    }
    public void postRequest(String url, MediaType mediaType, Object object, BaseCallBack callBack) {
        Request request = buildRequest(url, mediaType, object);
        doRequest(request, callBack);
    }



    private Request buildRequest(String url, MediaType mediaType, Object object) {
        Request.Builder builder = new Request.Builder()
                .url(url);
        RequestBody body = post(mediaType, object);
        builder.post(body);
        return builder.build();
    }

    private RequestBody post(MediaType mediaType, Object object) {
        return post(mediaType, object, -1, 0);
    }

    private RequestBody post(MediaType mediaType, Object object, int offset, int byteCount) {
        RequestBody mRequestBody;
        if (object != null) {
            if (object instanceof File) {
                mRequestBody = RequestBody.create(mediaType, (File) object);
            } else if (object instanceof String) {
                mRequestBody = RequestBody.create(mediaType, (String) object);
            } else if (object instanceof ByteString) {
                mRequestBody = RequestBody.create(mediaType, (ByteString) object);
            } else if (object instanceof byte[] && offset >= 0) {
                mRequestBody = RequestBody.create(mediaType, (byte[]) object, offset, byteCount);
            } else if (object instanceof byte[]) {
                mRequestBody = RequestBody.create(mediaType, (byte[]) object);
            } else {
                mRequestBody = RequestBody.create(mediaType, String.valueOf(object));
            }
            return mRequestBody;
        }
        return null;
    }


    private Request buildRequest(String url, Map<String, String> params, HttpMethodType httpMethodType) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (httpMethodType == HttpMethodType.GET) {
            builder.get();
        } else if (httpMethodType == HttpMethodType.POST) {
            RequestBody requestBody = buildFormData(params);
            builder.post(requestBody);
        }
        return builder.build();
    }


    private void doRequest(final Request request,okhttp3.Callback callback) {
        mOkHttpClient.newCall(request).enqueue(callback);
    }
    private void doRequest(final Request request, final BaseCallBack callBack) {
        callBack.OnRequestBefore(request);
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callBack.onResponse(response);
                String result = response.body().string();
                if (response.isSuccessful()) {
                    if (callBack.mType == String.class) {
                        callBackSuccess(callBack, call, response, result);
                    } else {
                        try {
                            Object object = mGson.fromJson(result, callBack.mType);
                            callBackSuccess(callBack, call, response, object);
                        } catch (JsonSyntaxException e) {
                            callBack.onError(call, response.code(), e);
                        }
                    }
                } else {
                    callBack.onError(call, response.code(), null);
                }
            }
        });
    }

    private void callBackSuccess(BaseCallBack callBack, Call call, Response response, Object result) {
        callBack.onSuccess(call, response, result);
    }

    private RequestBody buildFormData(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("platform", "Android")
                .add("version", "1.0")
                .add("key", "123456");
        if (params != null) {
            for (Map.Entry entry : params.entrySet()) {
                builder.add((String) entry.getKey(), (String) entry.getValue());
            }
        }
        return builder.build();
    }


    public enum HttpMethodType {
        GET, POST
    }
}
