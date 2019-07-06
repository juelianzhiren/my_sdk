package com.ztq.sdk.helper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import java.util.List;

/**
 * 线程管理器，将任务抛给UI主线程或者worker线程
 * @author hakonzhao
 *
 */
public class MyHandlerThread {
    private static final String TAG = "childedu.MyHandlerThread";

    private static HandlerThread thread1 = null;
    private static HandlerThread thread2 = null;
    private static Handler workerHandler1 = null;
    private static Handler workerHandler2 = null;
    private static Handler mainHandler = null;

    private void init() {
        mainHandler = null;
        workerHandler1 = null;
        workerHandler2 = null;
        thread1 = new HandlerThread("MyHandlerThread1", android.os.Process.THREAD_PRIORITY_DEFAULT);
        thread1.start();
        thread2 = new HandlerThread("MyHandlerThread2", android.os.Process.THREAD_PRIORITY_DEFAULT);
        thread2.start();
    }

    public MyHandlerThread() {
        init();
    }

    public Looper getLooper1() {
        return thread1.getLooper();
    }

    public Looper getLooper2() {
        return thread2.getLooper();
    }

    public static Handler getWorkerHandler1() {
        if (workerHandler1 == null) {
            workerHandler1 = new Handler(thread1.getLooper());
        }
        return workerHandler1;
    }

    public static Handler getWorkerHandler2() {
        if (workerHandler2 == null) {
            workerHandler2 = new Handler(thread2.getLooper());
        }
        return workerHandler2;
    }

    private static Handler getMainHandler() {
        if (mainHandler == null) {
            mainHandler = new Handler(Looper.getMainLooper());
        }
        return mainHandler;
    }

    public static int postToWorker1(final Runnable r) {
        if (r == null) {
            return -1;
        }
        getWorkerHandler1().post(r);
        return 0;
    }

    public static int postToWorker2(final Runnable r) {
        if (r == null) {
            return -1;
        }
        getWorkerHandler2().post(r);
        return 0;
    }

    public static int postToWorkerDelayed1(final Runnable r, long delayMillis) {
        if (r == null) {
            return -1;
        }
        getWorkerHandler1().postDelayed(r, delayMillis);
        return 0;
    }

    public static int postToWorkerDelayed2(final Runnable r, long delayMillis) {
        if (r == null) {
            return -1;
        }
        getWorkerHandler2().postDelayed(r, delayMillis);
        return 0;
    }

    public static boolean isMainThread() {
        return java.lang.Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    public boolean isThisHandlerThread1() {
        return java.lang.Thread.currentThread().getId() == thread1.getId();
    }

    public boolean isThisHandlerThread2() {
        return java.lang.Thread.currentThread().getId() == thread2.getId();
    }

    public static void postToMainThread(final Runnable run) {
        if (run == null) {
            return;
        }
        getMainHandler().post(run);
    }

    public static void postToMainThreadDelayed(final Runnable run, long delayed) {
        if (run == null) {
            return;
        }
        getMainHandler().postDelayed(run, delayed);
    }

    /**
     * 移除runnable列表中所有的callback
     * @param callbackList
     */
    public static void removeMainThreadCallbacks(List<Runnable> callbackList) {
        if (callbackList == null) {
            return;
        }
        for(int i = 0; i < callbackList.size(); i++) {
            getMainHandler().removeCallbacks(callbackList.get(i));
        }
    }
}