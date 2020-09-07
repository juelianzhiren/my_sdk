package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ztq.sdk.R;

/**
 * 简易的裁剪ImageView（还未完成）
 */
public class CropImageView extends ImageView {
    private static final String TAG = "noahedu.CropImageView";
    private Context mContext;
    private Rect mRect;
    /**
     * 是否默认居中
     */
    private boolean mIsDefaultCenter;
    /**
     * 默认裁剪宽度
     */
    private int mDefaultCropWidth;
    /**
     * 默认裁剪高度
     */
    private int mDefaultCropHeight;
    private Paint mRectPaint;
    private Paint mCornerPaint;
    private int mCornerWidth;
    private int mCornerStrokeWidth;
    private Paint mBackgroundPaint;
    /**
     * 是否在矩形的四个角附近
     */
    private boolean mIsNearbyCorner = false;
    /**
     * 在矩形的哪个角附近， 0代表左上角，1代表右上角， 2代表左下角，3代表右下角
     */
    private int mNearByCornerIndex = -1;
    /**
     * 是否在矩形内
     */
    private boolean mIsInsideRect = false;
    private static final int CORNER_INDEX_LEFT_TOP = 0;
    private static final int CORNER_INDEX_RIGHT_TOP = 1;
    private static final int CORNER_INDEX_LEFT_BOTTOM = 2;
    private static final int CORNER_INDEX_RIGHT_BOTTOM = 3;
    private static final int NEAR_BY_CORNER_MAXIMUM_DISTANCE = 50;
    private float mLastTouchX;
    private float mLastTouchY;
    private float mRectStrokeWidth = 2;
    private boolean mIsShowGridLine;
    private Paint mLinePaint;
    private boolean mContainTranslucentBackground = true;

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
        mDefaultCropWidth = (int) ta.getDimension(R.styleable.CropImageView_defaultCropWidth, getResources().getDimension(R.dimen.default_crop_width));
        mDefaultCropHeight = (int) ta.getDimension(R.styleable.CropImageView_defaultCropHeight, getResources().getDimension(R.dimen.default_crop_height));
        mCornerWidth = (int) ta.getDimension(R.styleable.CropImageView_defaultCropCornerWidth, getResources().getDimension(R.dimen.default_crop_corner_width));
        mCornerStrokeWidth = (int) ta.getDimension(R.styleable.CropImageView_defaultCropCornerStrokeWidth, getResources().getDimension(R.dimen.default_crop_corner_stroke_width));
        mIsShowGridLine = ta.getBoolean(R.styleable.CropImageView_defaultShowGridLine, true);
        ta.recycle();

        if (mDefaultCropWidth <= 0) {
            mDefaultCropWidth = 200;
        }
        if (mDefaultCropHeight <= 0) {
            mDefaultCropHeight = 200;
        }

        mRect = new Rect();
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
        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(mRectStrokeWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(mContext.getResources().getColor(R.color.gray));

        mRectPaint = new Paint();
        mRectPaint.setStrokeWidth(mRectStrokeWidth);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setColor(mContext.getResources().getColor(R.color.ivory));

        mCornerPaint = new Paint();
        mCornerPaint.setStyle(Paint.Style.FILL);
        mCornerPaint.setColor(mContext.getResources().getColor(R.color.white));

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mContext.getResources().getColor(R.color.translucent_black_95));
    }

    public void setContainTranslucentBackground(boolean mContainTranslucentBackground) {
        this.mContainTranslucentBackground = mContainTranslucentBackground;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mRect.left >= mRect.right || mRect.top >= mRect.bottom) {
            return;
        }
        drawGridLine(canvas);
        if (mContainTranslucentBackground) {
            drawTranslucentBackground(canvas);
        }
        drawRect(canvas);
        drawFourCorner(canvas);
    }

    private void drawGridLine(Canvas canvas) {
        if (!mIsShowGridLine) {
            return;
        }
        int size = 3;
        int width = getWidth();
        int height = getHeight();
        int distanceH = width / size;
        int distanceV = height / size;
        for (int i = 1; i < size; i++) {
            canvas.drawLine(i * distanceH, 0, i * distanceH, height, mLinePaint);
        }
        for (int i = 1; i < size; i++) {
            canvas.drawLine(0, i * distanceV, width, i * distanceV, mLinePaint);
        }
    }

    private void drawTranslucentBackground(Canvas canvas) {
        Rect leftRect = new Rect();
        Rect rightRect = new Rect();
        Rect topRect = new Rect();
        Rect bottomRect = new Rect();
        if (mRect.left > 0) {
            leftRect.left = 0;
            leftRect.right = mRect.left;
            leftRect.top = 0;
            leftRect.bottom = getHeight();
            canvas.drawRect(leftRect, mBackgroundPaint);
        }
        if (mRect.right < getWidth()) {
            rightRect.left = mRect.right;
            rightRect.right = getWidth();
            rightRect.top = 0;
            rightRect.bottom = getHeight();
            canvas.drawRect(rightRect, mBackgroundPaint);
        }
        if (mRect.top > 0) {
            topRect.left = mRect.left;
            topRect.right = mRect.right;
            topRect.top = 0;
            topRect.bottom = mRect.top;
            canvas.drawRect(topRect, mBackgroundPaint);
        }
        if (mRect.bottom < getHeight()) {
            bottomRect.left = mRect.left;
            bottomRect.right = mRect.right;
            bottomRect.top = mRect.bottom;
            bottomRect.bottom = getHeight();
            canvas.drawRect(bottomRect, mBackgroundPaint);
        }
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
        if (right >= mRect.right + mCornerStrokeWidth / 2) {
            right = mRect.right + mCornerStrokeWidth / 2;
        }
        top = mRect.top - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        Rect rect = new Rect(left, top, right, bottom);
        Log.v(TAG, "draw rect11, " + rect);
        canvas.drawRect(rect, mCornerPaint);
        right = left + mCornerStrokeWidth;
        bottom = top + mCornerWidth;
        if (bottom >= mRect.bottom + mCornerStrokeWidth / 2) {
            bottom = mRect.bottom + mCornerStrokeWidth / 2;
        }
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        Log.v(TAG, "draw rect12, " + rect);

        right = mRect.right + mCornerStrokeWidth / 2;
        left = right - mCornerWidth;
        if (left <= mRect.left - mCornerStrokeWidth / 2) {
            left = mRect.left - mCornerStrokeWidth / 2;
        }
        top = mRect.top - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        Log.v(TAG, "draw rect21, " + rect);
        left = mRect.right - mCornerStrokeWidth / 2;
        bottom = top + mCornerWidth;
        if (bottom >= mRect.bottom + mCornerStrokeWidth / 2) {
            bottom = mRect.bottom + mCornerStrokeWidth / 2;
        }
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        Log.v(TAG, "draw rect22, " + rect);

        left = mRect.left - mCornerStrokeWidth / 2;
        right = left + mCornerWidth;
        if (right >= mRect.right + mCornerStrokeWidth / 2) {
            right = mRect.right + mCornerStrokeWidth / 2;
        }
        top = mRect.bottom - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        Log.v(TAG, "draw rect31, " + rect);
        right = left + mCornerStrokeWidth;
        top = bottom - mCornerWidth;
        if (top <= mRect.top - mCornerStrokeWidth / 2) {
            top = mRect.top - mCornerStrokeWidth / 2;
        }
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        Log.v(TAG, "draw rect32, " + rect);

        right = mRect.right + mCornerStrokeWidth / 2;
        left = right - mCornerWidth;
        if (left <= mRect.left - mCornerStrokeWidth / 2) {
            left = mRect.left - mCornerStrokeWidth / 2;
        }
        top = mRect.bottom - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        Log.v(TAG, "draw rect41, " + rect);
        left = mRect.right - mCornerStrokeWidth / 2;
        top = bottom - mCornerWidth;
        if (top <= mRect.top - mCornerStrokeWidth / 2) {
            top = mRect.top - mCornerStrokeWidth / 2;
        }
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        Log.v(TAG, "draw rect42, " + rect);
    }

    private int getCropWidth() {
        return mRect.right - mRect.left;
    }

    private int getCropHeight() {
        return mRect.bottom - mRect.top;
    }

    public Bitmap getCropImage() {
        final Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }
        // Get image matrix values and place them in an array.
        float[] matrixValues = new float[9];
        getImageMatrix().getValues(matrixValues);
        for (int i = 0; i < matrixValues.length; i++) {
            Log.v(TAG, "matrixValues " + i + ": " + matrixValues[i]);
        }
        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        float scaleX = matrixValues[Matrix.MSCALE_X];
        float scaleY = matrixValues[Matrix.MSCALE_Y];
        float transX = matrixValues[Matrix.MTRANS_X];
        float transY = matrixValues[Matrix.MTRANS_Y];

        // Ensure that the left and top edges are not outside of the ImageView bounds.
        final float bitmapLeft = (transX < 0) ? Math.abs(transX) : 0;
        final float bitmapTop = (transY < 0) ? Math.abs(transY) : 0;
        // Get the original bitmap object.
        final Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        // Calculate the top-left corner of the crop window relative to the ~original~ bitmap size.
        final float cropX = (bitmapLeft + mRect.left) / scaleX;
        final float cropY = (bitmapTop + mRect.top) / scaleY;

        // Calculate the crop window size relative to the ~original~ bitmap size.
        // Make sure the right and bottom edges are not outside the ImageView bounds (this is just to address rounding discrepancies).
        final float cropWidth = Math.min(getCropWidth() / scaleX, originalBitmap.getWidth() - cropX);
        final float cropHeight = Math.min(getCropHeight() / scaleY, originalBitmap.getHeight() - cropY);

        Log.v(TAG, "CropX = " + cropX + "; cropY = " + cropY + "; cropWidth = " + cropWidth + "; cropHeight = " + cropHeight + "; mRect.left = " + mRect.left + "; mRet.top = " + mRect.top + "; originalBitmap.getWidth() = " +originalBitmap.getWidth() + "; originalBitmap.getHeight() = " + originalBitmap.getHeight());
        // Crop the subset from the original Bitmap.
//        return Bitmap.createBitmap(originalBitmap, mRect.left, mRect.top, (mRect.right - mRect.left) / scaleX, (mRect.bottom - mRect.top) / scaleY);
        return Bitmap.createBitmap(originalBitmap, (int) cropX, (int) cropY, (int) cropWidth, (int) cropHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        Log.v(TAG, "onTouchEvent, x = " + x + "; y = " + y + "; " + super.onTouchEvent(event));
        if (action == MotionEvent.ACTION_DOWN) {
            judgeLocation(x, y);
        } else if (action == MotionEvent.ACTION_MOVE) {
            if (mIsNearbyCorner) {
                if (mNearByCornerIndex == CORNER_INDEX_LEFT_TOP) {
                    if (mRect.right - x <= mRectStrokeWidth || mRect.bottom - y <= mRectStrokeWidth) {
                        return true;
                    }
                    mRect.left = (int) x;
                    mRect.top = (int) y;
                    invalidate();
                } else if (mNearByCornerIndex == CORNER_INDEX_RIGHT_TOP) {
                    if (x - mRect.right <= mRectStrokeWidth || mRect.bottom - y <= mRectStrokeWidth) {
                        return true;
                    }
                    mRect.right = (int) x;
                    mRect.top = (int) y;
                    invalidate();
                } else if (mNearByCornerIndex == CORNER_INDEX_LEFT_BOTTOM) {
                    if (mRect.right - x <= mRectStrokeWidth || y - mRect.top <= mRectStrokeWidth) {
                        return true;
                    }
                    mRect.left = (int) x;
                    mRect.bottom = (int) y;
                    invalidate();
                } else if (mNearByCornerIndex == CORNER_INDEX_RIGHT_BOTTOM) {
                    if (x - mRect.left <= mRectStrokeWidth || y - mRect.top <= mRectStrokeWidth) {
                        return true;
                    }
                    mRect.right = (int) x;
                    mRect.bottom = (int) y;
                    invalidate();
                }
            }
            Log.v(TAG, "mIsInsideRect = " + mIsInsideRect);
            if (mIsInsideRect) {
                float distanceX = x - mLastTouchX;
                float distanceY = y - mLastTouchY;
                if (mRect.left + distanceX <= 0) {
                    mRect.left = 0;
                    mRect.right += distanceX;
                    mRect.top += distanceY;
                    mRect.bottom += distanceY;
                    Log.v(TAG, "mRect = " + mRect);
                } else if (mRect.right + distanceX >= getWidth()) {
                    mRect.right = getWidth();
                    mRect.left += distanceX;
                    mRect.top += distanceY;
                    mRect.bottom += distanceY;
                } else if (mRect.top + distanceY <= 0) {
                    mRect.top = 0;
                    mRect.left += distanceX;
                    mRect.right += distanceX;
                    mRect.bottom += distanceY;
                } else if (mRect.bottom + distanceY >= getHeight()) {
                    mRect.bottom = getHeight();
                    mRect.left += distanceX;
                    mRect.right += distanceX;
                    mRect.top += distanceY;
                } else {
                    mRect.left += distanceX;
                    mRect.right += distanceX;
                    mRect.top += distanceY;
                    mRect.bottom += distanceY;
                }
                invalidate();
            }
        } else if (action == MotionEvent.ACTION_UP) {

        }
        mLastTouchX = x;
        mLastTouchY = y;
        return true;
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
            return;
        } else if (Math.sqrt(Math.pow(mRect.right - x, 2) + Math.pow(mRect.top - y, 2)) <= NEAR_BY_CORNER_MAXIMUM_DISTANCE) {
            mIsNearbyCorner = true;
            mNearByCornerIndex = CORNER_INDEX_RIGHT_TOP;
            return;
        } else if (Math.sqrt(Math.pow(mRect.left - x, 2) + Math.pow(mRect.bottom - y, 2)) <= NEAR_BY_CORNER_MAXIMUM_DISTANCE) {
            mIsNearbyCorner = true;
            mNearByCornerIndex = CORNER_INDEX_LEFT_BOTTOM;
            return;
        } else if (Math.sqrt(Math.pow(mRect.right - x, 2) + Math.pow(mRect.bottom - y, 2)) <= NEAR_BY_CORNER_MAXIMUM_DISTANCE) {
            mIsNearbyCorner = true;
            mNearByCornerIndex = CORNER_INDEX_RIGHT_BOTTOM;
            return;
        } else if (x > mRect.left && x < mRect.right && y > mRect.top && y < mRect.bottom) {
            mIsInsideRect = true;
            return;
        }
    }
}