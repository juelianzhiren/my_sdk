package com.noahedu.https;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.noahedu.https.utils.HttpsUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class VolleyHttps {

    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, -1);
    }

    public static RequestQueue newRequestQueue(Context context, int maxDiskCacheBytes) {

        SSLSocketFactory sslSocketFactory = initSSLSocketFactory(context);
        YxpHurlStack stack = new YxpHurlStack(null, sslSocketFactory);
        return Volley.newRequestQueue(context, stack, maxDiskCacheBytes);
    }

    private static SSLSocketFactory initSSLSocketFactory(Context context) {
        return new SSLSocketBuilder(context).getSSLSocketBuilder();
    }

    private static class SSLSocketBuilder {

        private Context context;

        public SSLSocketBuilder(Context context) {
            this.context = context;
        }

        public SSLSocketFactory getSSLSocketBuilder() {

            HttpsUtil httpsUtil = new HttpsUtil(context);
            X509TrustManager trustManager = httpsUtil.newTrustManager();
            SSLSocketFactory sslSocketFactory = httpsUtil.newSslSocketFactory(trustManager);
            return sslSocketFactory;
        }
    }

    public static class YxpHurlStack extends HurlStack {


        public YxpHurlStack(UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
            super(urlRewriter, sslSocketFactory);
        }

        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
            try {
                httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpsURLConnection;
        }

        // Let's assume your server app is hosting inside a server machine
        // which has a server certificate in which "Issued to" is "localhost",for example.
        // Then, inside verify method you can verify "localhost".
        // If not, you can temporarily return true
        private HostnameVerifier getHostnameVerifier() {
            return new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    //return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
                    HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
//                return hv.verify("localhost", session);
                    return true;
                }
            };
        }

    }
}
