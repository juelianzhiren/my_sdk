package com.ztq.sdk.entity;

import android.text.StaticLayout;

/**
 * Created by ztq on 2019/10/17.
 * 歌词entity，指的是每行歌词的实体类
 */
public class LyricsEntity implements Comparable<LyricsEntity> {
    /**歌词文本*/
    private String mContent;
    /**歌词开始时间(以毫秒为单位)*/
    private long mStartTime;
    /**距离顶部距离*/
    private float offset = Float.MIN_VALUE;
    private StaticLayout mStaticLayout;

    public LyricsEntity(String mContent, long mStartTime) {
        this.mContent = mContent;
        this.mStartTime = mStartTime;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public void setStartTime(long mStartTime) {
        this.mStartTime = mStartTime;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
    }

    @Override
    public int compareTo(LyricsEntity obj) {
        if (obj == null) {
            return -1;
        }
        return (int)(mStartTime - obj.getStartTime());
    }
}