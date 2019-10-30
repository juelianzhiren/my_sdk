package com.ztq.sdk.downloader;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ztq on 2019/10/30.
 * 下载管理器
 */
public class DownloadManager {
    private final String TAG = "noahedu.DownloadManager";
    private static DownloadManager mInstance;
    private static ExecutorService mFixedThreadPool;    // 创建一个定长线程池，可控制线程最大并发数为5，超出的线程会在队列中等待。
    private List<DownloadItem> mDownloadList;

    private DownloadManager() {
        mFixedThreadPool = Executors.newFixedThreadPool(5);
        mDownloadList = new ArrayList<>();
    }

    public DownloadManager getInstance(){
        if (mInstance == null) {
            synchronized (DownloadManager.class) {
                if (mInstance == null) {
                    mInstance = new DownloadManager();
                }
            }
        }
        return mInstance;
    }

    public void addDownloadTask(Context context, DownloadItem item) {
        if (context == null || item == null || mFixedThreadPool == null || mFixedThreadPool.isShutdown() || mFixedThreadPool.isTerminated()) {
            return;
        }
        DownloadRunnable runnable = new DownloadRunnable(context, item);
        mFixedThreadPool.execute(runnable);
    }

    public List<DownloadItem> getDownloadList() {
        return mDownloadList;
    }

    public void destroy() {
        if (mFixedThreadPool != null && !mFixedThreadPool.isShutdown() && !mFixedThreadPool.isTerminated()) {
            mFixedThreadPool.shutdown();
        }
        mInstance = null;
    }
}