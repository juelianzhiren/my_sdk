package com.ztq.sdk.api;

import android.util.ArrayMap;

import com.ztq.sdk.api.callback.CallBack;
import com.ztq.sdk.exception.AppException;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.FormBody;
import okhttp3.Handshake;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class BaseAPI<T> {
    private static final String TAG = "noahedu.BaseAPI";
    //使用okHttpClient请求
    protected  static OkHttpClient mOkHttpClient;
    private static Interceptor mInterceptor;
    private static ArrayMap<String, Call> mCallMap;
    private static EventListener mEventListener = new EventListener() {
        @Override
        public void callStart(Call call) {
            super.callStart(call);
            Log.v(TAG, "callStart");
        }

        @Override
        public void dnsStart(Call call, String domainName) {
            super.dnsStart(call, domainName);
            Log.v(TAG, "dnsStart");
        }

        @Override
        public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
            super.dnsEnd(call, domainName, inetAddressList);
            Log.v(TAG, "dnsEnd");
        }

        @Override
        public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
            super.connectStart(call, inetSocketAddress, proxy);
            Log.v(TAG, "connectStart");
        }

        @Override
        public void secureConnectStart(Call call) {
            super.secureConnectStart(call);
            Log.v(TAG, "secureConnectStart");
        }

        @Override
        public void secureConnectEnd(Call call, Handshake handshake) {
            super.secureConnectEnd(call, handshake);
            Log.v(TAG, "secureConnectEnd");
        }

        @Override
        public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
            super.connectEnd(call, inetSocketAddress, proxy, protocol);
            Log.v(TAG, "connectEnd");
        }

        @Override
        public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
            super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
            Log.v(TAG, "connectFailed");
        }

        @Override
        public void connectionAcquired(Call call, Connection connection) {
            super.connectionAcquired(call, connection);
            Log.v(TAG, "connectionAcquired");
        }

        @Override
        public void connectionReleased(Call call, Connection connection) {
            super.connectionReleased(call, connection);
            Log.v(TAG, "connectionReleased");
        }

        @Override
        public void requestHeadersStart(Call call) {
            super.requestHeadersStart(call);
            Log.v(TAG, "requestHeadersStart");
        }

        @Override
        public void requestHeadersEnd(Call call, Request request) {
            super.requestHeadersEnd(call, request);
            Log.v(TAG, "requestHeadersEnd");
        }

        @Override
        public void requestBodyStart(Call call) {
            super.requestBodyStart(call);
            Log.v(TAG, "requestBodyStart");
        }

        @Override
        public void requestBodyEnd(Call call, long byteCount) {
            super.requestBodyEnd(call, byteCount);
            Log.v(TAG, "requestBodyEnd");
        }

        @Override
        public void responseHeadersStart(Call call) {
            super.responseHeadersStart(call);
            Log.v(TAG, "responseHeadersStart");
        }

        @Override
        public void responseHeadersEnd(Call call, Response response) {
            super.responseHeadersEnd(call, response);
            Log.v(TAG, "responseHeadersEnd");
        }

        @Override
        public void responseBodyStart(Call call) {
            super.responseBodyStart(call);
            Log.v(TAG, "responseBodyStart");
        }

        @Override
        public void responseBodyEnd(Call call, long byteCount) {
            super.responseBodyEnd(call, byteCount);
            Log.v(TAG, "responseBodyEnd");
        }

        @Override
        public void callEnd(Call call) {
            super.callEnd(call);
            Log.v(TAG, "callEnd");
        }

        @Override
        public void callFailed(Call call, IOException ioe) {
            super.callFailed(call, ioe);
            Log.v(TAG, "callFailed");
        }
    };

    //设置默认超时时间为30秒
    static {
        initInterceptor();
        mOkHttpClient = new OkHttpClient.Builder()
                .eventListener(mEventListener)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
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
                e.printStackTrace();
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            if (e instanceof SocketTimeoutException) {
                                callback.onFailure(e, AppException.CODE_NETWORK_ERROR, e.getMessage());
                            } else {
                                callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.body() != null) {
                    final String msg = response.body().string();
                    Log.v(TAG, "code = " + response.code() + "; message = " + response.message() + "; body = " + msg);
                    MyHandlerThread.postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                int code = response.code();
                                if (code == HttpURLConnection.HTTP_OK) {
                                    callback.onSuccess(msg);
                                } else if (code == HttpURLConnection.HTTP_NOT_FOUND){
                                    callback.onFailure(new AppException(AppException.CODE_API_NOT_FOUND), AppException.CODE_API_NOT_FOUND, response.message());
                                } else if (code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * @param url
     * @param params
     * @param callback
     * @return void
     * @description: 网络异步请求方法（post方式，参数有数据量大的参数）
     */
    protected void asynPostWithLargeData(String url, Map<String, String> params, final CallBack<T> callback) {
        if (mOkHttpClient == null) {
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder = builder.addFormDataPart(entry.getKey(), entry.getValue());
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
                e.printStackTrace();
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            if (e instanceof SocketTimeoutException) {
                                callback.onFailure(e, AppException.CODE_NETWORK_ERROR, e.getMessage());
                            } else {
                                callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.body() != null) {
                    final String msg = response.body().string();
                    android.util.Log.v(TAG, "code = " + response.code() + "; message = " + response.message() + "; body = " + msg);
                    MyHandlerThread.postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                int code = response.code();
                                if (code == HttpURLConnection.HTTP_OK) {
                                    callback.onSuccess(msg);
                                } else if (code == HttpURLConnection.HTTP_NOT_FOUND){
                                    callback.onFailure(new AppException(AppException.CODE_API_NOT_FOUND), AppException.CODE_API_NOT_FOUND, response.message());
                                } else if (code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                } else if (code == HttpURLConnection.HTTP_BAD_GATEWAY) {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                } else {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * @param url
     * @param params
     * @param callback
     * @return void
     * @description: 网络异步请求方法（post方式携带图片文件）
     */
    protected void asynPostWithImageFiles(String url, Map<String, String> params, Map<String, File> fileMap, final CallBack<T> callback) {
        if (mOkHttpClient == null) {
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        if (fileMap != null) {
            for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                // MediaType.parse() 里面是上传的文件类型。
                File file = entry.getValue();
                RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
                String filename = file.getName();
                builder.addFormDataPart(entry.getKey(), filename, body);
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
                e.printStackTrace();
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            if (e instanceof SocketTimeoutException) {
                                callback.onFailure(e, AppException.CODE_NETWORK_ERROR, e.getMessage());
                            } else {
                                callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.body() != null) {
                    final String msg = response.body().string();
                    android.util.Log.v(TAG, "code = " + response.code() + "; message = " + response.message() + "; body = " + msg);
                    MyHandlerThread.postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                int code = response.code();
                                if (code == HttpURLConnection.HTTP_OK) {
                                    callback.onSuccess(msg);
                                } else if (code == HttpURLConnection.HTTP_NOT_FOUND){
                                    callback.onFailure(new AppException(AppException.CODE_API_NOT_FOUND), AppException.CODE_API_NOT_FOUND, response.message());
                                } else if (code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                } else if (code == HttpURLConnection.HTTP_BAD_GATEWAY) {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                } else {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                }
                            }
                        }
                    });
                }
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
                e.printStackTrace();
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            if (e instanceof SocketTimeoutException) {
                                callback.onFailure(e, AppException.CODE_NETWORK_ERROR, e.getMessage());
                            } else {
                                callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.body() != null) {
                    final String msg = response.body().string();
                    Log.v(TAG, "code = " + response.code() + "; message = " + response.message() + "; body = " + msg);
                    MyHandlerThread.postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                int code = response.code();
                                if (code == HttpURLConnection.HTTP_OK) {
                                    callback.onSuccess(msg);
                                } else if (code == HttpURLConnection.HTTP_NOT_FOUND){
                                    callback.onFailure(new AppException(AppException.CODE_API_NOT_FOUND), AppException.CODE_API_NOT_FOUND, response.message());
                                } else if (code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                }
                            }
                        }
                    });
                }
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
                            if (e instanceof SocketTimeoutException) {
                                callback.onFailure(e, AppException.CODE_NETWORK_ERROR, e.getMessage());
                            } else {
                                callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.body() != null) {
                    final String msg = response.body().string();
                    Log.v(TAG, "code = " + response.code() + "; message = " + response.message() + "; body = " + msg);
                    MyHandlerThread.postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                int code = response.code();
                                if (code == HttpURLConnection.HTTP_OK) {
                                    callback.onSuccess(msg);
                                } else if (code == HttpURLConnection.HTTP_NOT_FOUND){
                                    callback.onFailure(new AppException(AppException.CODE_API_NOT_FOUND), AppException.CODE_API_NOT_FOUND, response.message());
                                } else if (code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                }
                            }
                        }
                    });
                }
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
                e.printStackTrace();
                MyHandlerThread.postToMainThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            if (e instanceof SocketTimeoutException) {
                                callback.onFailure(e, AppException.CODE_NETWORK_ERROR, e.getMessage());
                            } else {
                                callback.onFailure(e, AppException.CODE_IO_ERROR, e.getMessage());
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.body() != null) {
                    final String msg = response.body().string();
                    Log.v(TAG, "code = " + response.code() + "; message = " + response.message() + "; body = " + msg);
                    MyHandlerThread.postToMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                int code = response.code();
                                if (code == HttpURLConnection.HTTP_OK) {
                                    callback.onSuccess(msg);
                                } else if (code == HttpURLConnection.HTTP_NOT_FOUND){
                                    callback.onFailure(new AppException(AppException.CODE_API_NOT_FOUND), AppException.CODE_API_NOT_FOUND, response.message());
                                } else if (code == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                                    callback.onFailure(new AppException(AppException.CODE_SERVER_ERROR), AppException.CODE_SERVER_ERROR, response.message());
                                }
                            }
                        }
                    });
                }
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