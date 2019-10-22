package com.ztq.sdk.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;

import com.ztq.sdk.R;
import com.ztq.sdk.entity.LyricsEntity;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.utils.Utils;

import java.util.List;

/**
 * Created by ztq on 2019/10/17.
 */
public class LyricsView extends View {
    private final String TAG = "noahedu.LyricsView";
    private Context mContext;
    private List<LyricsEntity> mLyricsList;
    private float mDividerHeight;          // 歌词之间的垂直间距
    private GestureDetector mGestureDetector;
    private int mOffset;
    private int offset1;
    private float mLyricsPadding;
    private int mTextGravity;
    private float mCurrentTextSize;
    private int  mCurrentTextColor;
    private float mNormalTextSize;
    private int mNormalTextColor;

    private int mTimelineColor;
    private int mTimeTextColor;
    private float mTimeTextWidth;

    private Paint.FontMetrics mTimeFontMetrics;

    private TextPaint mLrcPaint = new TextPaint();
    private TextPaint mTimePaint = new TextPaint();

    private int mCurrentLine = -1;   // 当前播放到第几句歌词了
    private boolean mIsShowTimeline = false;

    private ValueAnimator mAnimator;
    private int mAnimationDuration;

    private boolean mIsTouching = false;
    private SeekBarListener mSeekBarListener;

    private Runnable mHideTimeTextRunnable = new Runnable() {
        @Override
        public void run() {
            mIsShowTimeline = false;
            mIsTouching = false;
            invalidate();
        }
    };

    public LyricsView(Context context) {
        super(context);
        init(context, null);
    }

    public LyricsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LyricsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LyricsView);
        mDividerHeight = typedArray.getDimension(R.styleable.LyricsView_lrcDividerHeight, context.getResources().getDimension(R.dimen.lrc_divider_height));
        mLyricsPadding = typedArray.getDimension(R.styleable.LyricsView_lrcPadding, 0);
        mTextGravity = typedArray.getInteger(R.styleable.LyricsView_lrcTextGravity, LyricsEntity.GRAVITY_CENTER);
        mCurrentTextSize = typedArray.getDimension(R.styleable.LyricsView_lrcTextSize, getResources().getDimension(R.dimen.lrc_text_size));
        mCurrentTextColor = typedArray.getColor(R.styleable.LyricsView_lrcCurrentTextColor, getResources().getColor(R.color.blue));
        mNormalTextSize = typedArray.getDimension(R.styleable.LyricsView_lrcNormalTextSize, getResources().getDimension(R.dimen.lrc_text_size));
        mNormalTextColor = typedArray.getColor(R.styleable.LyricsView_lrcNormalTextColor, getResources().getColor(R.color.blue));
        float timeTextSize = typedArray.getDimension(R.styleable.LyricsView_lrcTimeTextSize, getResources().getDimension(R.dimen.lrc_time_text_size));
        float timelineHeight = typedArray.getDimension(R.styleable.LyricsView_lrcTimelineHeight, getResources().getDimension(R.dimen.lrc_timeline_height));
        mTimelineColor = typedArray.getColor(R.styleable.LyricsView_lrcTimelineColor, getResources().getColor(R.color.lrc_timeline_color));
        mTimeTextColor = typedArray.getColor(R.styleable.LyricsView_lrcTimeTextColor, getResources().getColor(R.color.lrc_time_text_color));
        int defDuration = getResources().getInteger(R.integer.lrc_animation_duration);
        mAnimationDuration = typedArray.getInt(R.styleable.LyricsView_lrcAnimationDuration, defDuration);
        mAnimationDuration = (mAnimationDuration < 0) ? defDuration : mAnimationDuration;
        Log.v(TAG, "mDividerHeight = " + mDividerHeight);
        typedArray.recycle();

        mTimeTextWidth = (int) getResources().getDimension(R.dimen.lrc_time_width);

        mGestureDetector = new GestureDetector(mContext, mSimpleOnGestureListener);
        mGestureDetector.setIsLongpressEnabled(false);

        mLrcPaint.setAntiAlias(true);
        mLrcPaint.setTextSize(mNormalTextSize);
        mLrcPaint.setColor(mNormalTextColor);
        mLrcPaint.setTextAlign(Paint.Align.LEFT);

        mTimePaint.setAntiAlias(true);
        mTimePaint.setTextSize(timeTextSize);
        mTimePaint.setTextAlign(Paint.Align.CENTER);
        //noinspection SuspiciousNameCombination
        mTimePaint.setStrokeWidth(timelineHeight);
        mTimePaint.setStrokeCap(Paint.Cap.ROUND);
        mTimeFontMetrics = mTimePaint.getFontMetrics();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.v(TAG, "view, height = " + getHeight());
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setSeekBarListener(SeekBarListener mSeekBarListener) {
        this.mSeekBarListener = mSeekBarListener;
    }

    private GestureDetector.SimpleOnGestureListener mSimpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.v(TAG, "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.v(TAG, "onScroll, distanceY = " + distanceY);
            offset1 += distanceY;
            mIsTouching = true;
            int currentline = getCurrentIndexFromOffset(offset1);
            Log.v(TAG, "onScroll currentline = " + currentline + "; offset1 = " + offset1);
            if (currentline != -1) {
                mCurrentLine = currentline;
                LyricsEntity entity = mLyricsList.get(currentline);
                LyricsEntity firstEntity = mLyricsList.get(0);
                if (entity != null && firstEntity != null) {
                    mOffset = getHeight() / 2 - (int) (entity.getOffset() - firstEntity.getOffset());
                }
                if (mCurrentLine < mLyricsList.size()) {
                    long time = mLyricsList.get(mCurrentLine).getStartTime();
                    if (mSeekBarListener != null) {
                        mSeekBarListener.seekTo(time);
                    }
                }
                invalidate();
            }
            invalidate();
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.v(TAG, "onFling");
            MyHandlerThread.postToMainThreadDelayed(mHideTimeTextRunnable, 2500);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.v(TAG, "onDown");
            if (hasLrc()) {
                MyHandlerThread.removeMainThreadRunnable(mHideTimeTextRunnable);
                mIsShowTimeline = true;
                invalidate();
                return true;
            }
            return true;
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v(TAG, "onTouchEvent");
        return mGestureDetector.onTouchEvent(event);
    }

    private int getCurrentIndexFromOffset(float offset1) {
        if (mLyricsList == null || mLyricsList.size() == 0) {
            return -1;
        }
        int result = -1;
        for(int i = 0; i < mLyricsList.size(); i++) {
            LyricsEntity entity = mLyricsList.get(i);
            if (entity != null) {
                float offset = entity.getOffset();
                if (offset < offset1) {
                    result = i;
                }
            }
        }
        return result;
    }

    public int getCurrentIndex(long currentTime) {
        if (mLyricsList == null || mLyricsList.size() == 0) {
            return -1;
        }
        int result = -1;
        for(int i = 0; i < mLyricsList.size(); i++) {
            LyricsEntity entity = mLyricsList.get(i);
            if (entity != null) {
                long startTime = entity.getStartTime();
                if (currentTime >= startTime) {
                    result = i;
                }
                if (Math.abs(currentTime - startTime) < 200) {
                    result = i;
                }
            }
        }
        return result;
    }

    /**
     * 结束滚动动画
     */
    private void endAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.end();
        }
    }

    public void updateOffset(int currentline) {
        if (currentline == -1 || mLyricsList == null || mLyricsList.size() == 0 || mLyricsList.size() <= currentline) {
            return;
        }
        LyricsEntity entity = mLyricsList.get(currentline);
        LyricsEntity firstEntity = mLyricsList.get(0);
        if (entity != null && firstEntity != null) {
            int offset = getHeight() / 2 - (int)(entity.getOffset() - firstEntity.getOffset());
            endAnimation();
            mAnimator = ValueAnimator.ofInt(mOffset, offset);
            mAnimator.setDuration(mAnimationDuration);
            mAnimator.setInterpolator(new LinearInterpolator());
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (!mIsTouching) {
                        mOffset = (int)animation.getAnimatedValue();
                        offset1 = mOffset;
                        invalidate();
                    }
                }
            });
            mAnimator.start();
        }
    }

    /**
     * 歌词是否有效
     *
     * @return true，如果歌词有效，否则false
     */
    public boolean hasLrc() {
        return !(mLyricsList == null || mLyricsList.isEmpty());
    }

    public void initEntryListStaticLayout() {
        if (!hasLrc() || getWidth() == 0) {
            return;
        }
        for (LyricsEntity lrcEntry : mLyricsList) {
            lrcEntry.initStaticLayout(mLrcPaint, (int) getLrcWidth(), mTextGravity);
        }
        mOffset = getHeight() / 2;
        offset1 = mOffset;
        for(int i = 0; i < mLyricsList.size(); i++) {
            setOffset(i);
        }
    }

    public void setCurrentLine(int currentLine) {
        mCurrentLine = currentLine;
    }

    /**
     * 获取歌词宽度
     */
    private float getLrcWidth() {
        return getWidth() - mLyricsPadding * 2;
    }

    /**
     * 获取歌词距离视图顶部的距离
     *
     */
    private void setOffset(int line) {
        if (mLyricsList.get(line).getOffset() == Float.MIN_VALUE) {
            float offset = getHeight() / 2;
            for (int i = 1; i <= line; i++) {
                offset += (mLyricsList.get(i - 1).getHeight() + mLyricsList.get(i).getHeight()) / 2 + mDividerHeight;
            }
            Log.v(TAG, "line = " + line + "; offset = " + offset);
            mLyricsList.get(line).setOffset(offset);
        }
    }

    public void setLyricsList(List<LyricsEntity> lyricsList){
        mLyricsList = lyricsList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerY = getHeight() / 2;
        float y = mOffset;
        Log.v(TAG, "mOffset = " + mOffset);
        if (mLyricsList != null) {
            for (int i = 0; i < mLyricsList.size(); i++) {
                if (i > 0) {
                    y += (mLyricsList.get(i - 1).getHeight() + mLyricsList.get(i).getHeight()) / 2 + mDividerHeight;
                }
                if (mLyricsList.get(i).getStaticLayout() != null) {
                    if (mCurrentLine == i) {
                        mLrcPaint.setColor(mCurrentTextColor);
                        mLrcPaint.setTextSize(mCurrentTextSize);
                    } else {
                        mLrcPaint.setColor(mNormalTextColor);
                        mLrcPaint.setTextSize(mNormalTextSize);
                    }
                    drawText(canvas, mLyricsList.get(i).getStaticLayout(), y);
                }
            }
        }

        if (mIsShowTimeline && mCurrentLine != -1) {
            mTimePaint.setColor(mTimelineColor);
            canvas.drawLine(mTimeTextWidth, centerY, getWidth() - mTimeTextWidth, centerY, mTimePaint);

            mTimePaint.setColor(mTimeTextColor);
            String timeText = Utils.getFormatTime(mLyricsList.get(mCurrentLine).getStartTime());
            float timeX = getWidth() - mTimeTextWidth / 2;
            float timeY = centerY - (mTimeFontMetrics.descent + mTimeFontMetrics.ascent) / 2;
            canvas.drawText(timeText, timeX, timeY, mTimePaint);
            Log.v(TAG, "timeText = " + timeText);
        }
    }

    /**
     * 画一行歌词
     *
     * @param y 歌词中心 Y 坐标
     */
    private void drawText(Canvas canvas, StaticLayout staticLayout, float y) {
        if (y <= -staticLayout.getHeight() / 2 || y >= getHeight() + staticLayout.getHeight() / 2) {    // 当歌词不在肉眼可见范围内，不画出
            return;
        }
        canvas.save();
        canvas.translate(mLyricsPadding, y - staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    public interface SeekBarListener {
        public void seekTo(long milliseconds);
    }
}