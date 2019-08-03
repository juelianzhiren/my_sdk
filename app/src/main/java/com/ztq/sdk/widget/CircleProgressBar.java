package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ztq.sdk.R;

/**
 * Created by ztq on 2019/8/1.
 * 记录进度条的圆圈View
 */
public class CircleProgressBar extends View {
    private final String TAG = "noahedu.CircleProgressBar";
    private Paint mBgPaint;
    private Paint mArcPaint;
    private int mMaxProgress = 100;
    private int mCurrentProgress = 0;
    private int mCircleBgColor = getContext().getResources().getColor(R.color.circle_bg);
    private int mArcColor = getContext().getResources().getColor(R.color.new_blue_bg);
    private int mStrokeWidth = 5;
    private int mRadius;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);
        mBgPaint.setStrokeWidth(mStrokeWidth);
        mBgPaint.setColor(mCircleBgColor);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);
        mArcPaint.setStrokeWidth(mStrokeWidth);
        mArcPaint.setColor(mArcColor);
    }

    public void setCircleBgColor(int mCircleBgColor) {
        this.mCircleBgColor = mCircleBgColor;
    }

    public void setArcColor(int mArcColor) {
        this.mArcColor = mArcColor;
    }

    public void setStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int mPointX;
        int mPointY;
        if (height > width) {
            mPointX = width / 2;
            mPointY = width / 2;
        } else {
            mPointX = height / 2;
            mPointY = height / 2;
        }
        mRadius = (width > height ? height / 2 : width / 2);
        RectF oval = new RectF(mPointX - mRadius, mPointY - mRadius, mPointX + mRadius, mPointY + mRadius);
        canvas.drawArc(oval, -90, 360, false, mBgPaint);

        mCurrentProgress = mCurrentProgress < 0 ? 0 : (mCurrentProgress > mMaxProgress ? mMaxProgress : mCurrentProgress);
        if (mMaxProgress != 0) {
            canvas.drawArc(oval, -90, (mCurrentProgress * 360) / mMaxProgress, false, mArcPaint);
        }
    }
}