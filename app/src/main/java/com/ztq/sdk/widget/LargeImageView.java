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

import com.ztq.sdk.model.MoveGestureDetector;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhy on 15/5/16.
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
    private Bitmap mBitmap;

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
            Log.v(TAG, "mImageWidth = " + mImageWidth + "; mImageHeight = " + mImageHeight);
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
        mDetector = new MoveGestureDetector(getContext(), new MoveGestureDetector.SimpleMoveGestureDetector() {
            @Override
            public boolean onMove(MoveGestureDetector detector) {
                int moveX = (int) detector.getMoveX();
                int moveY = (int) detector.getMoveY();
                Log.v(TAG, "onMove, moveX = " + moveX + "; moveY = " + moveY + "; mImageWidth = " + mImageWidth + "; width = " + getWidth() + "; mImageHeight = " + mImageHeight + "; height = " + getHeight());
                if (mImageWidth > getWidth()) {
                    mRect.offset(-moveX, 0);
                    checkWidth();
                    invalidate();
                }
                if (mImageHeight > getHeight()) {
                    mRect.offset(0, -moveY);
                    checkHeight();
                    invalidate();
                }
                return true;
            }
        });
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
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        mBitmap = mDecoder.decodeRegion(mRect, options);
        long t3 = System.currentTimeMillis();
        Log.v(TAG, "onDraw, get bitmap time = " + (t3 - t1) + "ms, bitmap size = " + mBitmap.getByteCount() / 1024.0 / 1024.0 + "m");
        canvas.drawBitmap(mBitmap, 0, 0, null);
        long t2 = System.currentTimeMillis();
        Log.v(TAG, "onDraw, duration time = " + (t2 - t1) + "ms");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int imageWidth = mImageWidth;
        int imageHeight = mImageHeight;

        //默认直接显示图片的中心区域，可以自己去调节
        mRect.left = imageWidth / 2 - width / 2;
        mRect.top = imageHeight / 2 - height / 2;
        mRect.right = mRect.left + width;
        mRect.bottom = mRect.top + height;
    }
}