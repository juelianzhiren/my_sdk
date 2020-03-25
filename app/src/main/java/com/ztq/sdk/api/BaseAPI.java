package com.ztq.sdk.api;

import android.util.ArrayMap;

import com.ztq.sdk.api.callback.CallBack;
import com.ztq.sdk.exception.AppException;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class BaseAPI<T> {
    private static final String TAG = "noahedu.BaseAPI";
    //使用okHttpClient请求
    protected  static OkHttpClient mOkHttpClient;
    private static Interceptor mInterceptor;
    private static ArrayMap<String, Call> mCallMap;

    //设置默认超时时间为30秒
    static {
        initInterceptor();
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(30,TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(mInterceptor)
                .build();
        mCallMap = new ArrayMap<String, Call>();
    }

    private static void initInterceptor() {
        mInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                long startTime = System.currentTimeMillis();
                okhttp3.Response response = chain.proceed(chain.request());
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                okhttp3.MediaType mediaType = response.body().contentType();
                String content = response.body().string();
                Log.v(TAG,"----------Request Start----------------");
                Log.v(TAG,"| " + request.toString());
                Log.v(TAG,"| Response:" + content);
                Log.v(TAG,"----------Request End:" + duration + "毫秒----------");
                return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
            }
        };
    }

    public BaseAPI() {

    }

    /**
     * @param url
     * @param params
     * @param callback
     * @return void
     * @description: 网络异步请求方法（post方式）
     */
    protected void asynPost(String url, Map<String, String> params, final CallBack<T> callback) {
        if (mOkHttpClient == null) {
            return;
        }
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder = builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = mOkHttpClient.newCall(request);
        mCallMap.put(url, call);
        if (callback != null) {
            callback.prepare();
        }
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                        }
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String msg = response.body().string();
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onSuccess(msg);
                        }
                    }
                });
            }
        });
    }

    /**
     * 网络异步请求方法（post方式，带有header部分）
     *
     * @param url
     * @param params
     * @param headersParam
     * @param callback
     */
    public void asyncPostWithHeaders(String url, Map<String, String> params, Map<String, String> headersParam, final CallBack<T> callback) {
        if (mOkHttpClient == null) {
            return;
        }
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder = builder.add(entry.getKey(), entry.getValue());
            }
        }

        Request.Builder builder1 = new Request.Builder().url(url);
        if (headersParam != null) {
            for (Map.Entry<String, String> entry : headersParam.entrySet()) {
                builder1 = builder1.addHeader(entry.getKey(), entry.getValue());

               Log.v(TAG, "key = " + entry.getKey() + "; value = " + entry.getValue());
            }
        }
        FormBody body = builder.build();
        Request request = builder1.post(body).build();
        Call call = mOkHttpClient.newCall(request);
        if (callback != null) {
            callback.prepare();
        }
        mCallMap.put(url, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                        }
                    }
                });

                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String msg = response.body().string();
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onSuccess(msg);
                        }
                    }
                });
            }
        });
    }

    /**
     * @param url
     * @param params
     * @param callback
     * @return void
     * @description: 网络异步请求方法（get方式）
     */
    public void asynGet(String url, Map<String, String> params, final CallBack<T> callback) {
        if (mOkHttpClient == null || Utils.isNullOrNil(url)) {
            return;
        }
        url = Utils.joinGetUrl(url, params);
        Log.v(TAG, "url = " + url);
        final Request request = new Request.Builder().url(url).get().build();
        Call call = mOkHttpClient.newCall(request);
        if (callback != null) {
            callback.prepare();
        }
        mCallMap.put(url, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                e.printStackTrace();
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String msg = response.body().string();
                Log.v(TAG, "message = " + response.message() + "; body = " + msg);
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onSuccess(msg);
                        }
                    }
                });
            }
        });
    }

    /**
     * 网络异步请求方法（get方式，带有header部分）
     *
     * @param url
     * @param headersParam
     * @param callback
     */
    public void asyncGetWithHeaders(String url, Map<String, String> params, Map<String, String> headersParam, final CallBack<T> callback) {
        if (mOkHttpClient == null) {
            return;
        }
        url = Utils.joinGetUrl(url, params);
        Request.Builder builder1 = new Request.Builder().url(url);
        if (headersParam != null) {
            for (Map.Entry<String, String> entry : headersParam.entrySet()) {
                builder1 = builder1.addHeader(entry.getKey(), entry.getValue());
            }
        }

        Request request = builder1.get().build();
        Call call = mOkHttpClient.newCall(request);
        if (callback != null) {
            callback.prepare();
        }
        mCallMap.put(url, call);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                        }
                    }
                });
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String msg = response.body().string();
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onSuccess(msg);
                        }
                    }
                });
            }
        });
    }

    protected void cancelRequest(String url) {
        if (mCallMap == null || Utils.isNullOrNil(url)) {
            return;
        }
        Call call = mCallMap.get(url);
        if (call != null) {
            call.cancel();
        }
    }

    protected void cancelAllRequests() {
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
        }
    }
}