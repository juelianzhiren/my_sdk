package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.ztq.sdk.R;

public class MyTextView extends TextView {
    private static final String TAG = "noahedu.MyTextView";
    private int flag = FLAG_NONE;
    public static final int FLAG_NONE = 0;
    public static final int FLAG_UNDER_DOT = 1;
    public static final int FLAG_UNDER_WAVE = 2;
    public static final int FLAG_UNDERLINE = 3;
    private Paint paint;
    private float dotRadius;
    private int dotColor;
    private int waveColor;
    private int underlineColor;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseSingleView);
        dotRadius = ta.getDimension(R.styleable.MyTextView_dotRadius, context.getResources().getDimension(R.dimen.dot_radius));
        dotColor = ta.getColor(R.styleable.MyTextView_dotColor, context.getResources().getColor(R.color.dot_color));
        waveColor = ta.getColor(R.styleable.MyTextView_waveColor, context.getResources().getColor(R.color.wave_color));
        underlineColor = ta.getColor(R.styleable.MyTextView_underlineColor, context.getResources().getColor(R.color.underline_color));
        ta.recycle();

        paint = new Paint();
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setDotRadius(float dotRadius) {
        this.dotRadius = dotRadius;
    }

    public void setDotColor(int dotColor) {
        this.dotColor = dotColor;
    }

    public void setWaveColor(int waveColor) {
        this.waveColor = waveColor;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String str = getText().toString();
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if (flag == FLAG_UNDER_DOT) {
            paint.setColor(dotColor);
            TextPaint textPaint = getPaint();
            for (int i = 0; i < str.length(); i++) {
                Layout layout = getLayout();
                Rect bound = new Rect();
                int line = layout.getLineForOffset(i);
                layout.getLineBounds(line, bound);

                float yAxisBottom = bound.bottom;//字符底部y坐标
                float left = bound.left;
                float right = bound.right;

                float xAxisLeft = layout.getPrimaryHorizontal(i);//字符左边x坐标

                float middle = xAxisLeft + textPaint.measureText(str.substring(i, i + 1)) / 2;
                canvas.drawCircle(middle, yAxisBottom - dotRadius, dotRadius, paint);

                Log.v(TAG, "bottom dot xAxisLeft = " + xAxisLeft + ", yAxisBottom = " + yAxisBottom + ", str = " + str + ", i = " + i + ", left = " + left + ", right = " + right);
            }
        } else if (flag == FLAG_UNDER_WAVE || flag == FLAG_UNDERLINE) {
            Layout layout = getLayout();
            int line = layout.getLineForOffset(str.length() - 1);

            Log.v(TAG, "bottom wave str = " + str + ", line = " + line);
            for (int i = 0; i <= line; i++) {
                Rect bound = new Rect();
                layout.getLineBounds(i, bound);
                float left = bound.left;
                float right = bound.right;

                if (flag == FLAG_UNDER_WAVE) {
                    Log.v(TAG, "bottom wave , str = " + str + ", i = " + i + ", left = " + left + ", right = " + right);
                    paint.setColor(waveColor);
                    drawTextUnderPath(canvas, bound.bottom - 4, (int) left, (int) right);
                } else if (flag == FLAG_UNDERLINE) {
                    Log.v(TAG, "underline , str = " + str + ", i = " + i + ", left = " + left + ", right = " + right);
                    paint.setColor(underlineColor);
                    canvas.drawLine(left, bound.bottom - 4, right, bound.bottom - 4, paint);
                }
            }
        }
    }

    private void drawTextUnderPath(Canvas canvas, int drawY, int startX, int endX) {
        final int step = 2;
        final int[] offArr = {-2, 0, 2, 0};
        int cycle = (endX - startX) / step;

        if (cycle < 1) {
            return;
        }

        Path wavePath = new Path();
        wavePath.moveTo(startX, drawY);
        int count = 1;
        while (cycle-- > 1) {
            startX += step;
            wavePath.lineTo(startX, drawY + offArr[count % 4]);
            count++;
        }
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(wavePath, paint);
    }
}