package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ztq.sdk.R;

/**
 * Created by ztq on 2019/11/21.
 * 有进度条显示的控件，这个控件将文本放在控件中间
 */
public class ProgressView extends View {
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
    private int mFillBeforeTextColor;
    private int mFillAfterTextColor;
    private float mTextSize;
    private String mText;

    public ProgressView(Context context) {
        this(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
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
        mFillBeforeTextColor = typedArray.getColor(R.styleable.ProgressView_fillBeforeTextColor, getResources().getColor(R.color.grey));
        mFillAfterTextColor = typedArray.getColor(R.styleable.ProgressView_fillAfterTextColor, getResources().getColor(R.color.black));
        mTextSize = typedArray.getDimension(R.styleable.ProgressView_textSize, getResources().getDimension(R.dimen.progress_button_text_size));
        typedArray.recycle();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mTextSize);
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
        super.onDraw(canvas);

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
        mPaint.setColor(mFillBeforeTextColor);
        if (TextUtils.isEmpty(mText)) {
            return;
        }

        mRectF.right = width * mProgress;
        float textWidth = mPaint.measureText(mText);
        float textLeft = width / 2 - textWidth / 2;
        float textRight = width / 2 + textWidth / 2;
        if (mRectF.right >= textRight) {//进度完全覆盖了文字，文字不用计算进度，全部显示白色
            mPaint.setColor(mFillAfterTextColor);
        } else if (mRectF.right > textLeft) {//进度覆盖了文字，但是没有完全覆盖，计算文字进度
            float textProgress = (mRectF.right - textLeft) / textWidth;
            LinearGradient textGradient = new LinearGradient(textLeft, 0, textRight, 0,
                    new int[]{ mFillAfterTextColor, mFillBeforeTextColor },
                    new float[]{textProgress, 0},
                    LinearGradient.TileMode.CLAMP);
            mPaint.setShader(textGradient);
        }
        canvas.drawText(mText, width / 2, mBaseLine, mPaint);
        mRectF.right = width;
    }

    public void setProgress(float progress) {
        this.mProgress = progress;
        invalidate();
    }

    public void setProgressAndText(float progress, String text) {
        this.mProgress = progress;
        this.mText = text;
        invalidate();
    }
}