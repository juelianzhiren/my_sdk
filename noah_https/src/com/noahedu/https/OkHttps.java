package com.noahedu.https;

import android.content.Context;

import com.noahedu.https.utils.HttpsUtil;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class OkHttps {

    private Context context;
    private OkHttpClient.Builder clientBuilder;
    // 默认使用https双向认证的链接
    private String[] hostNames;

    public static void toHttps(Context context, OkHttpClient.Builder clientBuilder, String... hostName) {
        toHttps(context, HttpsUtil.TYPE_HTTPS_BOTH_AUTH, clientBuilder, hostName);
    }

    public static void toHttps(Context context, int type, OkHttpClient.Builder clientBuilder, String... hostName) {
        new OkHttps(context, type, clientBuilder, hostName);
    }

    public OkHttps(Context context, int type, OkHttpClient.Builder clientBuilder, String... hostName) {
        this.context = context;
        if (hostName != null && hostName.length != 0) {
            hostNames = hostName;
        }
        this.clientBuilder = clientBuilder;
        initHttps(type);
    }

    private void initHttps(int type) {

        HttpsUtil httpsUtil = new HttpsUtil(context, type);
        X509TrustManager trustManager = httpsUtil.newTrustManager();
        SSLSocketFactory sslSocketFactory = httpsUtil.newSslSocketFactory(trustManager);
        if (sslSocketFactory != null) {
            clientBuilder.sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(new HttpsUtil.MyHostnameVerifier(hostNames));
        }
    }

}
