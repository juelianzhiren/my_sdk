package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.Button;

import com.ztq.sdk.R;
import com.ztq.sdk.log.Log;

/**
 * Created by ztq on 2019/11/21.
 */
public class ProgressButton extends Button {
    private final String TAG = "noahedu.DownloadButton";
    private Paint mPaint;
    private int mProgressAreaColor;
    private int mBlankAreaColor;
    private float mStrokeWidth;
    private float mRoundRadius;
    private float mBaseLine;
    private RectF mRectF;
    private float mProgress;
    private Context mContext;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
        mProgressAreaColor = typedArray.getColor(R.styleable.ProgressView_progressAreaColor, getResources().getColor(R.color.blue));
        mBlankAreaColor = typedArray.getColor(R.styleable.ProgressView_blankAreaColor, getResources().getColor(R.color.white));
        mStrokeWidth = typedArray.getDimension(R.styleable.ProgressView_strokeWidth, getResources().getDimension(R.dimen.progress_button_stroke_width));
        mRoundRadius = typedArray.getDimension(R.styleable.ProgressView_roundRadius, getResources().getDimension(R.dimen.progress_button_round_radius));
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.v(TAG, "onSizeChanged, w = " + w + "; h = " + h + "; mStrokeWidth = " + mStrokeWidth);
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(mStrokeWidth, mStrokeWidth, w - mStrokeWidth, h - mStrokeWidth);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        mBaseLine = h / 2 + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float height = mRectF.height();
        float width = mRectF.right;
        if (height <= 0 || width <= 0) {
            return;
        }
        mPaint.setShader(null);
        mPaint.setColor(mProgressAreaColor);

        if (mProgress > 1) {
            mProgress = 1;
        }
        if (mProgress == 1) {
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mPaint.setStyle(Paint.Style.STROKE);
        }
        canvas.drawRoundRect(mRectF, mRoundRadius, mRoundRadius, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        if (mProgress > 0 && mProgress < 1) {
            LinearGradient progressGradient = new LinearGradient(0, 0, width, 0,
                    new int[]{mProgressAreaColor, mBlankAreaColor},
                    new float[]{mProgress, 0},//两种颜色占的比重
                    LinearGradient.TileMode.CLAMP);
            mPaint.setShader(progressGradient);
            canvas.drawRoundRect(mRectF, mRoundRadius, mRoundRadius, mPaint);
            mPaint.setShader(null);
        }
        super.onDraw(canvas);
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }
}