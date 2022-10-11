package com.ztq.sdk.glide_test;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.ztq.sdk.api.BaseAPI;
import com.ztq.sdk.log.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpGlideUrlLoader implements ModelLoader<GlideUrl, InputStream> {
    private static final String TAG = "noahedu.OkHttpGlideUrlLoader";
    private OkHttpClient okHttpClient;

    private static EventListener mEventListener = new EventListener() {
        @Override
        public void callStart(Call call) {
            super.callStart(call);
            Log.v(TAG, "EventListener callStart");
        }

        @Override
        public void dnsStart(Call call, String domainName) {
            super.dnsStart(call, domainName);
            Log.v(TAG, "EventListener dnsStart, domainName = " + domainName);
        }

        @Override
        public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
            super.dnsEnd(call, domainName, inetAddressList);
            Log.v(TAG, "EventListener dnsEnd, domainName = " + domainName + "; inetAddressList = " + inetAddressList);
        }

        @Override
        public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
            super.connectStart(call, inetSocketAddress, proxy);
            Log.v(TAG, "EventListener connectStart, inetSocketAddress = " + inetSocketAddress + "; proxy = " + proxy);
        }

        @Override
        public void secureConnectStart(Call call) {
            super.secureConnectStart(call);
            Log.v(TAG, "EventListener secureConnectStart");
        }

        @Override
        public void secureConnectEnd(Call call, Handshake handshake) {
            super.secureConnectEnd(call, handshake);
            Log.v(TAG, "EventListener secureConnectEnd, handshake = " + handshake);
        }

        @Override
        public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
            super.connectEnd(call, inetSocketAddress, proxy, protocol);
            Log.v(TAG, "EventListener connectEnd, inetSocketAddress = " + inetSocketAddress + "; proxy = " + proxy + "; protocol = " + protocol);
        }

        @Override
        public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException ioe) {
            super.connectFailed(call, inetSocketAddress, proxy, protocol, ioe);
            Log.v(TAG, "EventListener connectFailed, inetSocketAddress = " + inetSocketAddress + "; proxy = " + proxy + "; protocol = " + protocol + "; ioe = " + ioe);
        }

        @Override
        public void connectionAcquired(Call call, Connection connection) {
            super.connectionAcquired(call, connection);
            Log.v(TAG, "EventListener connectionAcquired, connection = " + connection);
        }

        @Override
        public void requestHeadersStart(Call call) {
            super.requestHeadersStart(call);
            Log.v(TAG, "EventListener requestHeadersStart");
        }

        @Override
        public void requestHeadersEnd(Call call, Request request) {
            super.requestHeadersEnd(call, request);
            Log.v(TAG, "EventListener requestHeadersEnd");
        }

        @Override
        public void requestBodyStart(Call call) {
            super.requestBodyStart(call);
            Log.v(TAG, "EventListener requestBodyStart");
        }

        @Override
        public void requestBodyEnd(Call call, long byteCount) {
            super.requestBodyEnd(call, byteCount);
            Log.v(TAG, "EventListener requestBodyEnd, byteCount = " + byteCount);
        }

        @Override
        public void responseHeadersStart(Call call) {
            super.responseHeadersStart(call);
            Log.v(TAG, "EventListener responseHeadersStart");
        }

        @Override
        public void responseHeadersEnd(Call call, Response response) {
            super.responseHeadersEnd(call, response);
            Log.v(TAG, "EventListener responseHeadersEnd, responese = " + response);
        }

        @Override
        public void responseBodyStart(Call call) {
            super.responseBodyStart(call);
            Log.v(TAG, "EventListener responseBodyStart");
        }

        @Override
        public void responseBodyEnd(Call call, long byteCount) {
            super.responseBodyEnd(call, byteCount);
            Log.v(TAG, "EventListener responseBodyEnd, byteCount = " + byteCount);
        }

        @Override
        public void connectionReleased(Call call, Connection connection) {
            super.connectionReleased(call, connection);
            Log.v(TAG, "EventListener connectionReleased, connection = " + connection);
        }

        @Override
        public void callEnd(Call call) {
            super.callEnd(call);
            Log.v(TAG, "EventListener callEnd");
        }

        @Override
        public void callFailed(Call call, IOException ioe) {
            super.callFailed(call, ioe);
            Log.v(TAG, "EventListener callFailed");
        }
    };

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {

        private OkHttpClient client;

        public Factory() {
        }

        public Factory(OkHttpClient client) {
            this.client = client;
        }

        private synchronized OkHttpClient getOkHttpClient() {
            if (client == null) {
                client = new OkHttpClient();
                Interceptor mInterceptor = new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        long startTime = System.currentTimeMillis();
                        okhttp3.Response response = chain.proceed(chain.request());
                        long endTime = System.currentTimeMillis();
                        long duration = endTime - startTime;
                        okhttp3.MediaType mediaType = response.body().contentType();
                        String content = response.body().string();
                        String httplibValue = request.header("httplib");
                        Log.v(TAG, "key: httplib, value is = " + httplibValue);
                        return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
                    }
                };
//                client.interceptors().add(mInterceptor);
//                client = new OkHttpClient.Builder()
//                        .eventListener(mEventListener)
//                        .retryOnConnectionFailure(true)
//                        .addInterceptor(mInterceptor)
//                        .build();
            }
            return client;
        }

        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new OkHttpGlideUrlLoader(getOkHttpClient());
        }

        @Override
        public void teardown() {

        }
    }

    public OkHttpGlideUrlLoader(OkHttpClient client) {
        this.okHttpClient = client;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(GlideUrl model, int width, int height) {
        return new OkHttpFetcher(okHttpClient, model);
    }
}