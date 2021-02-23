package com.ztq.sdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载线程，利用原生下载
 */
public class NativeDownloadThread implements Runnable {
    private static final String TAG = "noahedu.NativeDownloadThread";
    private String mLocalPath;
    private String mRemoteUrl;
    private DownloadListener mDownloadListener;

    public NativeDownloadThread(String remoteUrl, String localPath, DownloadListener downloadListener) {
        this.mRemoteUrl = remoteUrl;
        this.mLocalPath = localPath;
        this.mDownloadListener = downloadListener;
    }

    @Override
    public void run() {
        if (!createFile()){
            if (mDownloadListener != null) {
                mDownloadListener.onFailure(mRemoteUrl);
            }
        }
        if (TextUtils.isEmpty(mRemoteUrl)) {
            if (mDownloadListener != null) {
                mDownloadListener.onFailure(mRemoteUrl);
            }
            return;
        }
        if (mDownloadListener != null) {
            mDownloadListener.onStart(mRemoteUrl);
        }
        final long startTime = System.currentTimeMillis();
        HttpURLConnection conn = null;
        InputStream is = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        File file = new File(mLocalPath);
        try {
            URL url = new URL(mRemoteUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept-Encoding", "musixmatch");
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");

            is = conn.getInputStream();

            byte[] bytes = Utils.inputStream2ByteArray(is);
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                Log.v(TAG, "bitmap size = " + bitmap);
                if (bitmap == null) {
                    byte[] arr = Base64.decode(bytes, Base64.DEFAULT);
                    Utils.saveByteArrayToFile(arr, file.getAbsolutePath());
                    if (mDownloadListener != null) {
                        mDownloadListener.onSuccess(mRemoteUrl);
                    }
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            if (mDownloadListener != null) {
                mDownloadListener.onSuccess(mRemoteUrl);
            }
            Log.v(TAG, "download success" + "totalTime=" + (System.currentTimeMillis() - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(bos);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private boolean createFile() {
        if (TextUtils.isEmpty(mLocalPath)) {
            return false;
        }
        File parentFile = new File(mLocalPath).getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        return true;
    }

    public interface DownloadListener {
        void onStart(String url);
        void onSuccess(String url);
        void onFailure(String url);
    }
}