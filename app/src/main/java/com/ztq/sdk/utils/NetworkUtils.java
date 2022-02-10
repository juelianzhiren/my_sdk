package com.ztq.sdk.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ztq.sdk.log.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

public class NetworkUtils {
    private static final String TAG = "noahedu.NetworkUtils";

    public static boolean isNetAvailable(Context context) {
        ConnectivityManager cManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cManager == null) {
            return false;
        } else {
            NetworkInfo[] info = cManager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static final boolean isWeakNetwork(Context context) {
        String urlPath = "http://www.baidu.com";
        final long t1 = System.currentTimeMillis();
        HttpURLConnection conn = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);
            conn.setRequestProperty("Accept-Encoding", "musixmatch");
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");

            is = conn.getInputStream();
            int responseCode = conn.getResponseCode();
            Object content = conn.getContent();
            long t2 = System.currentTimeMillis();
            Log.v(TAG, "responseCode = " + responseCode + "; content = " + content + "; " + (t2 - t1) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof SocketTimeoutException) {
                long t2 = System.currentTimeMillis();
                Log.e(TAG, "SocketTimeoutException, duration = " + (t2 - t1) + "ms");
                if (isNetAvailable(context)) {
                    return true;
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(bos);
            if (conn != null) {
                conn.disconnect();
            }
        }
        return false;
    }
}