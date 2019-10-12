package com.ztq.sdk.entity;

import android.graphics.Bitmap;

/**
 * Created by ztq on 2019/10/12.
 * BitmapUnit 是将 大Bitmap 分割为 N * M 个方块后的单个方块
 * 最多只保留3*3有效区域的BitmapUnit（即mBitmap对象存在且没有回收），其它区域的bitmap置于recycle状态
 */
public class BitmapUnit {
    /**占用的bitmap对象*/
    private Bitmap mBitmap;
     /** 是否正在加载bitmap */
    private boolean mIsLoading = false;
    /**行序号*/
    private int mRowIndex = 0;
    /**列序号*/
    private int mColumnIndex = 0;
    /**每行占用的像素值*/
    public static final int ROW_UNIT_LENGTH = 1000;
    /**每列占用的像素值*/
    public static final int COLUMN_UNIT_LENGTH = 1000;

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean mIsLoading) {
        this.mIsLoading = mIsLoading;
    }

    /**
     * 这里只回收正常的bitmap, 不回收缩略图的bitmap
     */
    private void recycle() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        mIsLoading = false;
    }
}