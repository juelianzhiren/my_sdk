package com.ztq.sdk.entity;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

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
    public static final int GRAVITY_CENTER = 0;
    public static final int GRAVITY_LEFT = 1;
    public static final int GRAVITY_RIGHT = 2;
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

    public int getHeight() {
        if (mStaticLayout == null) {
            return 0;
        }
        return mStaticLayout.getHeight();
    }

    public StaticLayout getStaticLayout() {
        return mStaticLayout;
    }

    @Override
    public int compareTo(LyricsEntity obj) {
        if (obj == null) {
            return -1;
        }
        return (int)(mStartTime - obj.getStartTime());
    }

    public void initStaticLayout(TextPaint paint, int width, int gravity) {
        Layout.Alignment align;
        switch (gravity) {
            case GRAVITY_LEFT:
                align = Layout.Alignment.ALIGN_NORMAL;
                break;
            default:
            case GRAVITY_CENTER:
                align = Layout.Alignment.ALIGN_CENTER;
                break;
            case GRAVITY_RIGHT:
                align = Layout.Alignment.ALIGN_OPPOSITE;
                break;
        }
        mStaticLayout = new StaticLayout(mContent, paint, width, align, 1f, 0f, false);
        offset = Float.MIN_VALUE;
    }
}