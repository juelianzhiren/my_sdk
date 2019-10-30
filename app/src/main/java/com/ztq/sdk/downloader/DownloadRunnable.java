package com.ztq.sdk.downloader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ztq.sdk.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ztq on 2019/10/30.
 * 下载线程，利用android原生进行下载
 */
public class DownloadRunnable implements Runnable {
    private final String TAG = "noahedu.DownloadRunnable";
    private Context mContext;
    private DownloadItem mItem;

    public DownloadRunnable(Context context, DownloadItem item) {
        mContext = context;
        mItem = item;
    }

    @Override
    public void run() {
        if (mContext == null || mItem == null || mItem.getState() == DownloadItem.STATE_FINISHED) {
            return;
        }
        getRemoteFileSizeAndCreateTempFile();
        if (mItem.getTotalSize() <= 0) {
            Utils.showToast(mContext, "获取文件大小失败！");
            return;
        }
        mItem.setState(DownloadItem.STATE_DOWNLOADING);

        HttpURLConnection conn = null;
        RandomAccessFile accessFile = null;
        InputStream is = null;
        long currentSize = 0;
        long totalSize = 0;
        long startSize = 0;
        int state = DownloadItem.STATE_DOWNLOADING;
        try {
            URL url = new URL(mItem.getRemoteUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept-Encoding", "musixmatch");
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Range", "bytes=" + mItem.getCurrentSize() + "-" + mItem.getTotalSize());
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
            // conn.setRequestProperty("Connection", "Keep-Alive");

            accessFile = new RandomAccessFile(mItem.getLocalTempPath(), "rwd");
            accessFile.seek(mItem.getCurrentSize());

            currentSize = mItem.getCurrentSize();
            totalSize = mItem.getTotalSize();
            startSize = currentSize;

            is = conn.getInputStream();

            byte[] buffer = new byte[4096];
            int length = -1;
            long startTime = System.currentTimeMillis();
            int speed = 0;

            while ((length = is.read(buffer)) != -1) {
                state = mItem.getState();
                if (state == DownloadItem.STATE_PAUSE) {
                    mItem.setCurrentSize(currentSize);
                    synchronized (mItem) {
                        try {
                            mItem.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                currentSize += length;
                // Log.d(TAG, "length=" + length);
                accessFile.write(buffer, 0, length);

                // update database per 100K.
                if (currentSize - mItem.getCurrentSize() >= 102400) {
                    mItem.setCurrentSize(currentSize);
                    mItem.setState(DownloadItem.STATE_DOWNLOADING);
                    sendBroadcast(Constants.ACTION_DOWNLOAD_UPDATE, mItem);
                }
            }
            is.close();
            accessFile.close();
            conn.disconnect();

            //将下载的临时文件重命名为本地正式文件名
            File sourceFile = new File(mItem.getLocalTempPath());
            File targetFile = new File(mItem.getLocalPath());
            sourceFile.renameTo(targetFile);

            mItem.setCurrentSize(currentSize);
            mItem.setState(DownloadItem.STATE_FINISHED);
            sendBroadcast(Constants.ACTION_DOWNLOAD_FINISH, mItem);
            Log.d(TAG, "download finished ");

        } catch (Exception e) {
            Log.e(TAG, "download exception : " + e.getMessage() + "; url = " + mItem.getRemoteUrl());
            e.printStackTrace();
            mItem.setState(DownloadItem.STATE_PAUSE);
            mItem.setCurrentSize(currentSize);
            sendBroadcast(Constants.ACTION_DOWNLOAD_NETWORK_ERROR, mItem);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (accessFile != null) {
                    accessFile.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送广播
     * @param type
     * @param item
     */
    private void sendBroadcast(String type, DownloadItem item) {
        if (item == null || Utils.isNullOrNil(type)) {
            return;
        }
        Intent intent = new Intent(type);
        if (!Utils.isNullOrNil(item.getFromPackageName())) {
            intent.setPackage(item.getFromPackageName());
        }
        intent.putExtra(Constants.INTENT_KEY_ITEM, item);
        mContext.sendBroadcast(intent);
    }

    /**
     * 获取远程文件的大小及创建临时文件
     */
    private void getRemoteFileSizeAndCreateTempFile() {
        if (mItem == null || Utils.isNullOrNil(mItem.getRemoteUrl()) || Utils.isNullOrNil(mItem.getLocalTempPath())){
            return;
        }
        HttpURLConnection conn = null;
        try {
            URL url = new URL(mItem.getRemoteUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestProperty("Accept-Encoding", "musixmatch");
            conn.setRequestMethod("HEAD");

            int fileSize = conn.getContentLength();
            Log.i(TAG, "file url = " + mItem.getRemoteUrl() + "; total size[" + fileSize + "]");
            if(fileSize > 0){
                mItem.setTotalSize(fileSize);
                File parentFile = new File(mItem.getLocalTempPath()).getParentFile();
                if (!parentFile.exists()) {
                    parentFile.mkdirs();
                }
                File file = new File(mItem.getLocalTempPath());

                if (!file.exists()) {
                    file.createNewFile();
                    mItem.setCurrentSize(0);
                } else {
                    mItem.setCurrentSize(file.length());
                }
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            Log.e(TAG, "createFile MalformedURLException", e);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "createFile FileNotFoundException", e);
        } catch (IOException e) {
            Log.e(TAG, "createFile IOException", e);
        }
    }
}