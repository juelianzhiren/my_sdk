package com.ztq.sdk.entity;

import java.io.Serializable;

public class ResultLaunchAds extends ResultBase implements Serializable{
    private LaunchAdsInfo data;

    public LaunchAdsInfo getData() {
        return data;
    }

    public void setData(LaunchAdsInfo data) {
        this.data = data;
    }

    public static class LaunchAdsInfo implements Serializable {
        private int id;
        private String name;
        private String imgHorizontal;
        private String imgVertical;
        private int goType;
        private int goId;
        private int detailCount;
        private Object applist;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgHorizontal() {
            return imgHorizontal;
        }

        public void setImgHorizontal(String imgHorizontal) {
            this.imgHorizontal = imgHorizontal;
        }

        public String getImgVertical() {
            return imgVertical;
        }

        public void setImgVertical(String imgVertical) {
            this.imgVertical = imgVertical;
        }

        public int getGoType() {
            return goType;
        }

        public void setGoType(int goType) {
            this.goType = goType;
        }

        public int getGoId() {
            return goId;
        }

        public void setGoId(int goId) {
            this.goId = goId;
        }

        public int getDetailCount() {
            return detailCount;
        }

        public void setDetailCount(int detailCount) {
            this.detailCount = detailCount;
        }

        public Object getApplist() {
            return applist;
        }

        public void setApplist(Object applist) {
            this.applist = applist;
        }
    }
}