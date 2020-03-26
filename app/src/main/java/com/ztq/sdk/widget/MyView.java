package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.ztq.sdk.log.Log;

/**
 * Created by ztq on 2019/7/29.
 */
public class MyView extends TextView {
    private final String TAG = "noahedu.MyView";
    private TextPaint mPaint;
    private StaticLayout mStaticLayout;
    private TextPaint mTextPaint;

    public MyView(Context context) {
        this(context, null);
        Log.v(TAG, "MyView Constructor one parammeter");
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.v(TAG, "MyView Constructor two parammeters");
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.v(TAG, "MyView Constructor three parammeter");
        init(context);
    }

    private void init(Context context) {
        mPaint = new TextPaint();
        mPaint.setTextAlign(Paint.Align.RIGHT);//主要是这里的取值不一样
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(80);

        mTextPaint = new TextPaint();
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(80);

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mStaticLayout = new StaticLayout("居中显示", mTextPaint, getWidth(), Layout.Alignment.ALIGN_CENTER, 1f, 0f, false);
                invalidate();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    @Override
    protected void onFinishInflate() {
        Log.v(TAG, "MyView onFinishInflate");
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.v(TAG, "MyView onMeasure");
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.v(TAG, "MyView onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        Log.v(TAG, "MyView onFocusChanged, focused = " + focused);
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        Log.v(TAG, "MyView onWindowFocusChanged, hasWindowFocus = " + hasWindowFocus);
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        Log.v(TAG, "MyView onWindowVisibilityChanged, visibility = " + visibility);
        super.onWindowVisibilityChanged(visibility);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        Log.v(TAG, "MyView onVisibilityChanged, visibility = " + visibility);
        super.onVisibilityChanged(changedView, visibility);
    }

    @Override
    protected void onAttachedToWindow() {
        Log.v(TAG, "MyView onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.v(TAG, "MyView onDetachedFromWindow");
        super.onDetachedFromWindow();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.v(TAG, "MyView onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.v(TAG, "MyView onDraw");
        super.onDraw(canvas);
        int baseLineY = 200;

        canvas.drawText("abcdefghijkl's", 200, baseLineY, mPaint);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();

        float top = fontMetrics.top + baseLineY;
        float ascent = fontMetrics.ascent + baseLineY;
        float descent = fontMetrics.descent + baseLineY;
        float bottom = fontMetrics.bottom + baseLineY;

        //绘制基线
        mPaint.setColor(Color.parseColor("#FF1493"));
        canvas.drawLine(0, baseLineY, getWidth(), baseLineY, mPaint);

        //绘制top直线
        mPaint.setColor(Color.parseColor("#FFB90F"));
        canvas.drawLine(0, top, getWidth(), top, mPaint);

        //绘制ascent直线
        mPaint.setColor(Color.parseColor("#b03060"));
        canvas.drawLine(0, ascent, getWidth(), ascent, mPaint);

        //绘制descent直线
        mPaint.setColor(Color.parseColor("#912cee"));
        canvas.drawLine(0, descent, getWidth(), descent, mPaint);

        //绘制bottom直线
        mPaint.setColor(Color.parseColor("#1E90FF"));
        canvas.drawLine(0, bottom, getWidth(), bottom, mPaint);

        if (getWidth() * getHeight() != 0 && mStaticLayout != null) {
            canvas.save();
            canvas.translate(0, getHeight() / 2 - mStaticLayout.getHeight() / 2);
            mStaticLayout.draw(canvas);
            canvas.restore();
        }
    }
}