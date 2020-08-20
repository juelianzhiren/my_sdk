package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ztq.sdk.R;
import com.ztq.sdk.utils.Utils;

/**
 * 简易的裁剪ImageView（还未完成）
 */
public class CropImageView extends ImageView {
    private static final String TAG = "noahedu.CropImageView";
    private Context mContext;
    private Rect mRect;
    /**是否默认居中*/
    private boolean mIsDefaultCenter;
    /**默认裁剪宽度*/
    private int mDefaultCropWidth;
    /**默认裁剪高度*/
    private int mDefaultCropHeight;
    private Paint mRectPaint;
    private Paint mCornerPaint;
    private int mCornerWidth;
    private int mCornerStrokeWidth = 6;
    private Paint mBackgroundPaint;
    /**是否在矩形的四个角附近*/
    private boolean mIsNearbyCorner = false;
    /**在矩形的哪个角附近， 0代表左上角，1代表右上角， 2代表左下角，3代表右下角*/
    private int mNearByCornerIndex = -1;
    /**是否在矩形内*/
    private boolean mIsInsideRect = false;
    private static final int CORNER_INDEX_LEFT_TOP = 0;
    private static final int CORNER_INDEX_RIGHT_TOP = 1;
    private static final int CORNER_INDEX_LEFT_BOTTOM = 2;
    private static final int CORNER_INDEX_RIGHT_BOTTOM = 3;
    private static final int NEAR_BY_CORNER_MAXIMUM_DISTANCE = 10;

    public CropImageView(Context context) {
        this(context, null);
    }

    public CropImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
        addListener();
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CropImageView);
        mIsDefaultCenter = ta.getBoolean(R.styleable.CropImageView_isDefaultCenter, true);
        mDefaultCropWidth = (int)ta.getDimension(R.styleable.CropImageView_defaultCropWidth, getResources().getDimension(R.dimen.default_crop_width));
        mDefaultCropHeight = (int)ta.getDimension(R.styleable.CropImageView_defaultCropHeight, getResources().getDimension(R.dimen.default_crop_height));
        ta.recycle();

        if (mDefaultCropWidth <= 0) {
            mDefaultCropWidth = 200;
        }
        if (mDefaultCropHeight <= 0) {
            mDefaultCropHeight = 200;
        }

        mRect = new Rect();
        mCornerWidth = Utils.dp2px(mContext, 15);

        initPaint();
    }

    private void addListener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = getWidth();
                int height = getHeight();
                Log.v(TAG, "width = " + width + "; height = " + height);
                if (mDefaultCropWidth > width) {
                    mDefaultCropWidth = width;
                }
                if (mDefaultCropHeight > height) {
                    mDefaultCropHeight = height;
                }
                mRect.left = (width - mDefaultCropWidth) / 2;
                mRect.right = mRect.left + mDefaultCropWidth;
                mRect.top = (height - mDefaultCropHeight) / 2;
                mRect.bottom = mRect.top + mDefaultCropHeight;
                invalidate();
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    private void initPaint() {
        mRectPaint = new Paint();
        mRectPaint.setStrokeWidth(2);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setColor(mContext.getResources().getColor(R.color.ivory));

        mCornerPaint = new Paint();
        mCornerPaint.setStyle(Paint.Style.FILL);
        mCornerPaint.setColor(mContext.getResources().getColor(R.color.white));

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mContext.getResources().getColor(R.color.translucent_black_95));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawTranslucentBackground(canvas);
        drawRect(canvas);
        drawFourCorner(canvas);
    }

    private void drawTranslucentBackground(Canvas canvas) {
        Rect rect = new Rect();
        rect.top = 0;
        rect.left = 0;
        rect.bottom = getHeight();
        rect.right = getWidth();
        canvas.drawRect(rect, mBackgroundPaint);
    }

    private void drawRect(Canvas canvas) {
        canvas.drawRect(mRect, mRectPaint);
    }

    private void drawFourCorner(Canvas canvas) {
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;
        left = mRect.left - mCornerStrokeWidth / 2;
        right = left + mCornerWidth;
        top = mRect.top - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        right = left + mCornerStrokeWidth;
        bottom = top + mCornerWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);

        right = mRect.right + mCornerStrokeWidth / 2;
        left = right - mCornerWidth;
        top = mRect.top - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        left = mRect.right - mCornerStrokeWidth / 2;
        bottom = top + mCornerWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);

        left = mRect.left - mCornerStrokeWidth / 2;
        right = left + mCornerWidth;
        top = mRect.bottom - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        right = left + mCornerStrokeWidth / 2;
        top = bottom - mCornerWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);

        right = mRect.right + mCornerStrokeWidth / 2;
        left = right - mCornerWidth;
        top = mRect.bottom - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        left = mRect.right - mCornerStrokeWidth / 2;
        top = bottom - mCornerWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            judgeLocation(x, y);
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (mIsNearbyCorner) {
                if (mNearByCornerIndex == CORNER_INDEX_LEFT_TOP) {
                    mRect.left = (int) x;
                    mRect.top = (int) y;
                    invalidate();
                } else if (mNearByCornerIndex == CORNER_INDEX_RIGHT_TOP) {
                    mRect.right = (int) x;
                    mRect.top = (int) y;
                    invalidate();
                } else if (mNearByCornerIndex == CORNER_INDEX_LEFT_BOTTOM) {
                    mRect.left = (int) x;
                    mRect.bottom = (int) y;
                    invalidate();
                } else if (mNearByCornerIndex == CORNER_INDEX_RIGHT_BOTTOM) {
                    mRect.right = (int) x;
                    mRect.bottom = (int) y;
                    invalidate();
                }
            }
        } else if (action == MotionEvent.ACTION_UP) {

        }
        return super.onTouchEvent(event);
    }

    private void judgeLocation(float x, float y) {
        mIsInsideRect = false;
        mIsNearbyCorner = false;
        mNearByCornerIndex = -1;
        if (mRect == null) {
            return;
        }
        if (Math.sqrt(Math.pow(mRect.left - x, 2) + Math.pow(mRect.top - y, 2)) <= NEAR_BY_CORNER_MAXIMUM_DISTANCE) {
            mIsNearbyCorner = true;
            mNearByCornerIndex = CORNER_INDEX_LEFT_TOP;
        } else if (Math.sqrt(Math.pow(mRect.right - x, 2) + Math.pow(mRect.top - y, 2)) <= NEAR_BY_CORNER_MAXIMUM_DISTANCE) {
            mIsNearbyCorner = true;
            mNearByCornerIndex = CORNER_INDEX_RIGHT_TOP;
        } else if (Math.sqrt(Math.pow(mRect.left - x, 2) + Math.pow(mRect.bottom - y, 2)) <= NEAR_BY_CORNER_MAXIMUM_DISTANCE) {
            mIsNearbyCorner = true;
            mNearByCornerIndex = CORNER_INDEX_LEFT_BOTTOM;
        } else if (Math.sqrt(Math.pow(mRect.right - x, 2) + Math.pow(mRect.bottom - y, 2)) <= NEAR_BY_CORNER_MAXIMUM_DISTANCE) {
            mIsNearbyCorner = true;
            mNearByCornerIndex = CORNER_INDEX_RIGHT_BOTTOM;
        }
    }
}