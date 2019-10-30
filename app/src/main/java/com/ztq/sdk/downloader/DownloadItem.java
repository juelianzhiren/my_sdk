package com.ztq.sdk.downloader;

/**
 * Created by ztq on 2019/10/30.
 * 下载项
 */
public class DownloadItem {
    /**名字*/
    private String name;
    /**下载的远程url*/
    private String remoteUrl;
    /**下载到本地的文件path（完整）*/
    private String localPath;
    /**下载完成之前的临时文件path*/
    private String localTempPath;
    /**文件大小*/
    private long totalSize;
    /**已下载好的大小*/
    private long currentSize;
    /**类型*/
    private int type;
    /**下载状态*/
    private int state;

    public static final int TYPE_APK = 0x1;      // apk文件
    public static final int TYPE_AUDIO = 0x2;    // 音频文件
    public static final int TYPE_VIDEO = 0x3;    // 视频文件
    public static final int TYPE_DOCUMENT = 0x4; // 文档文件

    public static final int STATE_WAITING = 0x0;      // 等待下载
    public static final int STATE_DOWNLOADING = 0x1;  // 正在下载
    public static final int STATE_PAUSE = 0x2;        // 暂停下载
    public static final int STATE_STOP = 0x03;        // 下载停止
    public static final int STATE_FINISH = 0x4;       // 下载完成
    public static final int STATE_FAILED = 0x5;       // 下载失败

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getLocalTempPath() {
        return localTempPath;
    }

    public void setLocalTempPath(String localTempPath) {
        this.localTempPath = localTempPath;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(long currentSize) {
        this.currentSize = currentSize;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DownloadItem)) {
            return false;
        }
        DownloadItem item = (DownloadItem) obj;
        return remoteUrl.equalsIgnoreCase(item.remoteUrl) && type == item.type;
    }
}