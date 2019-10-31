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

    public static DownloadManager getInstance(){
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
        for(int i = 0; i < mDownloadList.size(); i++) {
            DownloadItem item1 = mDownloadList.get(i);
            if (item1.equals(item)) {     // 如果之前已经添加了下载任务，则直接返回
                return;
            }
        }
        mDownloadList.add(item);
        DownloadRunnable runnable = new DownloadRunnable(context, item);
        mFixedThreadPool.execute(runnable);
    }

    public void pauseDownloadTask(DownloadItem item) {
        if (item == null) {
            return;
        }
        item.setState(DownloadItem.STATE_PAUSE);
    }

    public void resumeDownloadTask(DownloadItem item) {
        if (item == null) {
            return;
        }
        item.setState(DownloadItem.STATE_DOWNLOADING);
        item.notify();
    }

    public void removeTask(DownloadItem item) {
        mDownloadList.remove(item);
    }

    public void removeAllTask() {
        for(int i = 0; i < mDownloadList.size(); i++) {
            DownloadItem item1 = mDownloadList.get(i);
            mDownloadList.remove(item1);
        }
    }

    public List<DownloadItem> getDownloadList() {
        return mDownloadList;
    }

    public void destroy() {
        if (mFixedThreadPool != null && !mFixedThreadPool.isShutdown() && !mFixedThreadPool.isTerminated()) {
            mFixedThreadPool.shutdownNow();
        }
        mInstance = null;
    }
}