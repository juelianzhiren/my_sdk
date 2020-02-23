package com.ztq.sdk.entity;

/**
 * 时间轴项信息
 */
public class TimelineItem {
    private String content;
    private String dateInfo;
    private String img;
    private boolean isFocus;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDateInfo() {
        return dateInfo;
    }

    public void setDateInfo(String dateInfo) {
        this.dateInfo = dateInfo;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }
}