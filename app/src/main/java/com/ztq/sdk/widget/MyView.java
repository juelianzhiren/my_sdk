package com.ztq.sdk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ztq on 2019/7/29.
 */
public class MyView extends TextView {
    private final String TAG = "noahedu.MyView";
    private TextPaint mPaint;

    public MyView(Context context) {
        this(context, null);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        mPaint = new TextPaint();
        mPaint.setTextAlign(Paint.Align.RIGHT);//主要是这里的取值不一样
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(80);
    }

    @Override
    protected void onDraw(Canvas canvas) {
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
    }
}