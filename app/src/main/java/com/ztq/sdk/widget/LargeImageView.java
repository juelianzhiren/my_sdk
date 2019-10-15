package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.ztq.sdk.entity.BitmapUnit;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.model.MoveGestureDetector;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ztq on 19/10/15.
 */
public class LargeImageView extends View {
    private final String TAG = "noahedu.LargeImageView";
    private BitmapRegionDecoder mDecoder;
    /**
     * 图片的宽度和高度
     */
    private int mImageWidth, mImageHeight;
    /**
     * 绘制的区域
     */
    private volatile Rect mRect = new Rect();

    private MoveGestureDetector mDetector;

    private static final BitmapFactory.Options options = new BitmapFactory.Options();

    private int mArrSizeW = 1;        // 二维数组长度
    private int mArrSizeH = 1;       // 二维数组宽度
    private BitmapUnit[][] mBitmapUnitArrs;   // 这里做一个处理，数组中最多只保留3*3个有效元素（即元素内的Bitmap属性不为空且不被回收）

    private Rect mSrcRect;
    private Rect mTarRect;

    static {
        options.inPreferredConfig = Bitmap.Config.RGB_565;
    }

    public void setInputStream(InputStream is) {
        try {
            BitmapFactory.Options tmpOptions = new BitmapFactory.Options();
            tmpOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, null, tmpOptions);
            mImageWidth = tmpOptions.outWidth;
            mImageHeight = tmpOptions.outHeight;
            tmpOptions.inJustDecodeBounds = false;
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
            requestLayout();
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void init() {
        mSrcRect = new Rect();
        mTarRect = new Rect();
        mDetector = new MoveGestureDetector(getContext(),
                new MoveGestureDetector.SimpleMoveGestureDetector() {
            @Override
            public boolean onMove(MoveGestureDetector detector) {
                Log.v(TAG, "onMove");
                int moveX = (int) detector.getMoveX();
                int moveY = (int) detector.getMoveY();
                if (mImageWidth > getWidth()) {
                    mRect.offset(-moveX, 0);
                    checkWidth();
                }
                if (mImageHeight > getHeight()) {
                    mRect.offset(0, -moveY);
                    checkHeight();
                }
                updateBitmapUnitArrs();
                invalidate();
                return true;
            }
        });
    }

    /**
     * 更新BitmapUnitArrs数组里的值，主要是将mRect为中心的3*3矩形内bitmap赋值（如果为空的话）(3width * 3height)
     * ，区域以外的bitmap回收（如果bitmap不为空的话）
     */
    private void updateBitmapUnitArrs() {
        for (int i = 0; i < mBitmapUnitArrs.length; i++) {
            for (int j = 0; j < mBitmapUnitArrs[i].length; j++) {
                final BitmapUnit unit = mBitmapUnitArrs[i][j];
                final int i1 = i;
                final int j1 = j;
                if (unit != null) {
                    Bitmap bitmap = unit.getBitmap();
                    final Rect rect = unit.getRect();
                    if (rect != null) {
                        Log.v(TAG, "updateBitmapUnitArrs, i = " + i + "; j = " + j + "; bitmap = " + bitmap);
                        if (isBelongTo3By3Area(rect, mRect)) {
                            if (bitmap == null && !unit.isLoading()) {
                                MyHandlerThread.postToWorker1(new Runnable() {
                                    @Override
                                    public void run() {
                                        unit.setIsLoading(true);
                                        Bitmap bitmap0 = mDecoder.decodeRegion(rect, options);
                                        Log.v(TAG, "i = " + i1 + "; j = " + j1 + ";" + "updateBitmapUnitArrs, decodeRegion");
                                        unit.setBitmap(bitmap0);
                                        unit.setIsLoading(false);
                                    }
                                });
                            }
                        } else {
                            if (bitmap != null && !bitmap.isRecycled()) {
                                bitmap.recycle();
                                bitmap = null;
                                unit.setBitmap(null);
                                Log.v(TAG, "i = " + i + "; j = " + j + ", bitmap recycle");
                            }
                        }
                    }
                }
            }
        }
    }

    private void checkWidth() {
        Rect rect = mRect;
        int imageWidth = mImageWidth;

        if (rect.right > imageWidth) {
            rect.right = imageWidth;
            rect.left = imageWidth - getWidth();
        }
        if (rect.left < 0) {
            rect.left = 0;
            rect.right = getWidth();
        }
    }

    private void checkHeight() {
        Rect rect = mRect;
        int imageHeight = mImageHeight;

        if (rect.bottom > imageHeight) {
            rect.bottom = imageHeight;
            rect.top = imageHeight - getHeight();
        }
        if (rect.top < 0) {
            rect.top = 0;
            rect.bottom = getHeight();
        }
    }

    public LargeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        addListener();
    }

    private void addListener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = getMeasuredWidth();
                int height = getMeasuredHeight();
                generateData(width, height);
                invalidate();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG, "onTouchEvent, " + ((event.getAction() == MotionEvent.ACTION_DOWN) ? "action_down" : (event.getAction() == MotionEvent.ACTION_MOVE) ? "action_move" : "action_up or action_cancel"));
        mDetector.onToucEvent(event);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long t1 = System.currentTimeMillis();
//        mBitmap = mDecoder.decodeRegion(mRect, options);
//        long t3 = System.currentTimeMillis();
//        Log.v(TAG, "onDraw, get bitmap time = " + (t3 - t1) + "ms, bitmap size = " + mBitmap
//        .getByteCount() / 1024.0 / 1024.0 + "m");
//        canvas.drawBitmap(mBitmap, 0, 0, null);
//        long t2 = System.currentTimeMillis();
//        Log.v(TAG, "onDraw, duration time = " + (t2 - t1) + "ms");

        if (mBitmapUnitArrs != null) {
            for (int i = 0; i < mBitmapUnitArrs.length; i++) {
                for (int j = 0; j < mBitmapUnitArrs[i].length; j++) {
                    BitmapUnit unit = mBitmapUnitArrs[i][j];
                    if (unit != null) {
                        Rect rect = unit.getRect();
                        Bitmap bitmap = unit.getBitmap();
                        if (bitmap != null && rect != null) {
                            if (rect.intersects(mRect.left, mRect.top, mRect.right, mRect.bottom)) {
                                int left = rect.left < mRect.left ? mRect.left : rect.left;
                                int top = rect.top < mRect.top ? mRect.top : rect.top;
                                int right = rect.right > mRect.right ? mRect.right : rect.right;
                                int bottom = rect.bottom > mRect.bottom ? mRect.bottom : rect.bottom;
                                Log.v(TAG, "drawBitmap, left = " + left + "; top = " + top + "; " + "right = " + right + "; bottom = " + bottom);

                                mSrcRect.left = left - j * BitmapUnit.ROW_UNIT_PIXELS_EXCEPT_LAST;
                                mSrcRect.top = top - i * BitmapUnit.COLUMN_UNIT_PIXELS_EXCEPT_LAST;
                                mSrcRect.right = right - j * BitmapUnit.ROW_UNIT_PIXELS_EXCEPT_LAST;
                                mSrcRect.bottom = bottom - i * BitmapUnit.COLUMN_UNIT_PIXELS_EXCEPT_LAST;
                                mTarRect.left = left - mRect.left;
                                mTarRect.top = top - mRect.top;
                                mTarRect.right = right - mRect.left;
                                mTarRect.bottom = bottom - mRect.top;
//                                    Rect srcRect = new Rect(left - j * BitmapUnit
//                                    .ROW_UNIT_PIXELS_EXCEPT_LAST, top - i * BitmapUnit
//                                    .COLUMN_UNIT_PIXELS_EXCEPT_LAST, right - j * BitmapUnit
//                                    .ROW_UNIT_PIXELS_EXCEPT_LAST, bottom - i * BitmapUnit
//                                    .COLUMN_UNIT_PIXELS_EXCEPT_LAST);
//                                    Rect tarRect = new Rect(left - mRect.left, top - mRect.top,
//                                    right - mRect.left, bottom - mRect.top);
                                canvas.drawBitmap(bitmap, mSrcRect, mTarRect, null);
                            }
                        }
                    }
                }
            }
        }
    }

    private void generateData(int width, int height) {
        BitmapUnit.ROW_UNIT_PIXELS_EXCEPT_LAST = width;
        BitmapUnit.ROW_LAST_UNIT_PIXELS = width;
        if (mImageWidth > width) {
            if (mImageWidth % width == 0) {
                mArrSizeW = mImageWidth / width;
            } else {
                mArrSizeW = mImageWidth / width + 1;
                BitmapUnit.ROW_LAST_UNIT_PIXELS = mImageWidth - (mArrSizeW - 1) * width;
            }
        }
        BitmapUnit.COLUMN_UNIT_PIXELS_EXCEPT_LAST = height;
        BitmapUnit.COLUMN_LAST_UNIT_PIXELS = height;
        if (mImageHeight > height) {
            if (mImageHeight % height == 0) {
                mArrSizeH = mImageHeight / height;
            } else {
                mArrSizeH = mImageHeight / height + 1;
                BitmapUnit.COLUMN_LAST_UNIT_PIXELS = mImageHeight - (mArrSizeH - 1) * height;
            }
        }

        //默认直接显示图片的中心区域，可以自己去调节
        if (mImageWidth > width) {
            mRect.left = mImageWidth / 2 - width / 2;
            mRect.right = mRect.left + width;
        } else {
            mRect.left = 0;
            mRect.right = width;
        }
        if (mImageHeight > height) {
            mRect.top = mImageHeight / 2 - height / 2;
            mRect.bottom = mRect.top + height;
        } else {
            mRect.top = 0;
            mRect.bottom = height;
        }

        mBitmapUnitArrs = new BitmapUnit[mArrSizeH][mArrSizeW];
        Log.v(TAG, "width = " + width + "; height = " + height + "; mArrSizeW = " + mArrSizeW + "; mArrSizeH = " + mArrSizeH);
        Log.v(TAG, "mRect, left = " + mRect.left + "; right = " + mRect.right + "; top = " + mRect.top + "; bottom = " + mRect.bottom);
        for (int i = 0; i < mArrSizeH; i++) {
            for (int j = 0; j < mArrSizeW; j++) {
                BitmapUnit unit = new BitmapUnit();
                unit.setRowIndex(i);
                unit.setColumnIndex(j);
                unit.setIsLoading(false);
                Rect rect = new Rect();
                rect.left = j * BitmapUnit.ROW_UNIT_PIXELS_EXCEPT_LAST;
                if (j != mArrSizeW - 1) {
                    rect.right = (j + 1) * BitmapUnit.ROW_UNIT_PIXELS_EXCEPT_LAST;
                } else {
                    rect.right = mImageWidth;
                }
                rect.top = i * BitmapUnit.COLUMN_UNIT_PIXELS_EXCEPT_LAST;
                if (i != mArrSizeH - 1) {
                    rect.bottom = (i + 1) * BitmapUnit.COLUMN_UNIT_PIXELS_EXCEPT_LAST;
                } else {
                    rect.bottom = mImageHeight;
                }
                Bitmap bitmap = null;
                if (isBelongTo3By3Area(rect, mRect)) {
                    unit.setIsLoading(true);
                    bitmap = mDecoder.decodeRegion(rect, options);
                    unit.setBitmap(bitmap);
                    unit.setIsLoading(false);
                }
                Log.v(TAG, "i = " + i + "; j = " + j + "; bitmap = " + bitmap + "; left = " + rect.left + "; right = " + rect.right + "; top = " + rect.top + "; bottom = " + rect.bottom);
                unit.setRect(rect);
                mBitmapUnitArrs[i][j] = unit;
            }
        }
    }

    /**
     * sourceRect是否在以targetRect为中心的3 ROW_UNIT_PIXELS_EXCEPT_LAST * 3
     * COLUMN_UNIT_PIXELS_EXCEPT_LAST矩形内(3width * 3height)或者相交
     *
     * @param sourceRect
     * @param targetRect
     * @return
     */
    private boolean isBelongTo3By3Area(Rect sourceRect, Rect targetRect) {
        if (sourceRect == null || targetRect == null) {
            return false;
        }
        Rect _3By3Rect = new Rect(targetRect.left - BitmapUnit.ROW_UNIT_PIXELS_EXCEPT_LAST, targetRect.top - BitmapUnit.COLUMN_UNIT_PIXELS_EXCEPT_LAST, targetRect.right + BitmapUnit.ROW_UNIT_PIXELS_EXCEPT_LAST, targetRect.bottom + BitmapUnit.COLUMN_UNIT_PIXELS_EXCEPT_LAST);
        Log.v(TAG, "isBelongTo3By3Area before, left = " + sourceRect.left + "; right = " + sourceRect.right + "; top = " + sourceRect.top + "; bottom = " + sourceRect.bottom);
        boolean flag = _3By3Rect.intersect(sourceRect);
        Log.v(TAG, "isBelongTo3By3Area after, left = " + sourceRect.left + "; right = " + sourceRect.right + "; top = " + sourceRect.top + "; bottom = " + sourceRect.bottom);
        return flag;
    }

    public void clear() {
        mDecoder.recycle();
        for(int i = 0; i < mBitmapUnitArrs.length; i++) {
            for(int j = 0; j < mBitmapUnitArrs[i].length; j++) {
                BitmapUnit unit = mBitmapUnitArrs[i][j];
                Bitmap bitmap = unit.getBitmap();
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        }
    }
}