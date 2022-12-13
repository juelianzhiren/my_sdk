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

import com.noahedu.core_front_auto_tp.R;

/**
 * 简易的裁剪ImageView (使用autosize会影响到CropImageView的显示，所以尽量别用)
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
    /**
     * 旋转的角度
     */
    private float mRotateDegree;
    private int mWidth;
    private int mHeight;
    private Bitmap mBitmap;

    private double mRadioInFitCenter = 1.0f;

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

    public void setDefaultCropSize(int mDefaultCropWidth, int mDefaultCropHeight) {
        this.mDefaultCropWidth = mDefaultCropWidth;
        this.mDefaultCropHeight = mDefaultCropHeight;
        invalidate();
    }

    public void setCornerStrokeWidth(int mCornerStrokeWidth) {
        this.mCornerStrokeWidth = mCornerStrokeWidth;
    }

    public void setCornerWidth(int mCornerWidth) {
        this.mCornerWidth = mCornerWidth;
    }

    public void setIsShowGridLine(boolean mIsShowGridLine) {
        this.mIsShowGridLine = mIsShowGridLine;
    }

    public void continueRotateDegree(float rotateDegree) {
        mRotateDegree += rotateDegree;
        Matrix matrix = new Matrix();
        matrix.postRotate(rotateDegree);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
        setImageBitmap(mBitmap);
        invalidate();
    }

    private void addListener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWidth = getWidthInFact();
                mHeight = getHeightInFact();
                int measureWidth = getMeasuredWidth();
                int measureHeight = getMeasuredHeight();
                BitmapDrawable drawable = (BitmapDrawable) getDrawable();
                Log.v(TAG, "width = " + getWidth() + "; height = " + getHeight() + "; measureWidth = " + measureWidth + "; measureHeight = " + measureHeight + "; widthInFact = " + mWidth + "; heightInFact = " + mHeight);
                if (drawable != null) {
                    Bitmap bitmap = drawable.getBitmap();
                    if (bitmap != null) {
                        Log.v(TAG, "; bitmap width = " + bitmap.getWidth() + "; height = " + bitmap.getHeight());
                    }
                }

                if (mDefaultCropWidth == 0) {
                    mDefaultCropWidth = (int)mContext.getResources().getDimension(R.dimen.default_crop_width);
                }
                if (mDefaultCropHeight == 0) {
                    mDefaultCropHeight = (int)mContext.getResources().getDimension(R.dimen.default_crop_height);
                }
                if (mDefaultCropWidth > mWidth) {
                    mDefaultCropWidth = mWidth;
                }
                if (mDefaultCropHeight > mHeight) {
                    mDefaultCropHeight = mHeight;
                }
                mRect.left = (mWidth - mDefaultCropWidth) / 2 + (getWidth() - getWidthInFact()) / 2;
                mRect.right = mRect.left + mDefaultCropWidth;
                mRect.top = (mHeight - mDefaultCropHeight) / 2 + (getHeight() - getHeightInFact()) / 2;
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
        mRectPaint.setColor(0xff898bf8);

        mCornerPaint = new Paint();
        mCornerPaint.setStyle(Paint.Style.FILL);
        mCornerPaint.setColor(0xff898bf8);

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mContext.getResources().getColor(R.color.translucent_black_80));
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
//        drawGridLine(canvas);
        if (mContainTranslucentBackground) {
            drawTranslucentBackground(canvas);
        }
        drawRect(canvas);
        drawFourCorner(canvas);

        try {
            Drawable drawable = getDrawable();
            mBitmap = ((BitmapDrawable) drawable).getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawGridLine(Canvas canvas) {
        if (!mIsShowGridLine) {
            return;
        }
        int size = 3;
        int width = getWidthInFact();
        int height = getHeightInFact();
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
        if (mRect.left > (getWidth() - getWidthInFact()) / 2) {
            leftRect.left = (getWidth() - getWidthInFact()) / 2;
            leftRect.right = mRect.left;
            leftRect.top = (getHeight() - getHeightInFact()) / 2;
            leftRect.bottom = (getHeight() + getHeightInFact()) / 2;
            canvas.drawRect(leftRect, mBackgroundPaint);
        }
        if (mRect.right < (getWidth() + getWidthInFact()) / 2) {
            rightRect.left = mRect.right;
            rightRect.right = (getWidth() + getWidthInFact()) / 2;
            rightRect.top = (getHeight() - getHeightInFact()) / 2;
            rightRect.bottom = (getHeight() + getHeightInFact()) / 2;
            canvas.drawRect(rightRect, mBackgroundPaint);
        }
        if (mRect.top > (getHeight() - getHeightInFact()) / 2) {
            topRect.left = mRect.left;
            topRect.right = mRect.right;
            topRect.top = (getHeight() - getHeightInFact()) / 2;
            topRect.bottom = mRect.top;
            canvas.drawRect(topRect, mBackgroundPaint);
        }
        if (mRect.bottom < (getHeight() + getHeightInFact()) / 2) {
            bottomRect.left = mRect.left;
            bottomRect.right = mRect.right;
            bottomRect.top = mRect.bottom;
            bottomRect.bottom = (getHeight() + getHeightInFact()) / 2;
            canvas.drawRect(bottomRect, mBackgroundPaint);
        }
    }

    private double getRadio() {
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        double radio = 1.0;
        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            if (bitmap != null) {
                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();
                int width = getWidth();
                int height = getHeight();
                if ((double)width / bitmapWidth > (double) height / bitmapHeight) {
                    radio = (double)height / bitmapHeight;
                } else {
                    radio = (double) width / bitmapWidth;
                }
            }
        }
        return radio;
    }

    /**
     * 获取实际上的宽度
     * @return
     */
    private int getWidthInFact() {
        if (getScaleType() == ScaleType.FIT_CENTER) {   // 对fitCenter特殊处理
            mRadioInFitCenter = getRadio();
            BitmapDrawable drawable = (BitmapDrawable) getDrawable();
            if (drawable != null) {
                Bitmap bitmap = drawable.getBitmap();
                if (bitmap != null) {
                    int width = (int)(mRadioInFitCenter * bitmap.getWidth());
                    if (width >= getWidth()) {
                        return getWidth();
                    }
                    return width;
                }
            }
        }
        return getWidth();
    }

    /**
     * 获取实际上的高度
     */
    private int getHeightInFact() {
        if (getScaleType() == ScaleType.FIT_CENTER) {   // 对fitCenter特殊处理
            mRadioInFitCenter = getRadio();
            BitmapDrawable drawable = (BitmapDrawable) getDrawable();
            if (drawable != null) {
                Bitmap bitmap = drawable.getBitmap();
                if (bitmap != null) {
                    int height = (int)(mRadioInFitCenter * bitmap.getHeight());
                    if (height >= getHeight()) {
                        return getHeight();
                    }
                    return height;
                }
            }
        }
        return getHeight();
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
        canvas.drawRect(rect, mCornerPaint);
        right = left + mCornerStrokeWidth;
        bottom = top + mCornerWidth;
        if (bottom >= mRect.bottom + mCornerStrokeWidth / 2) {
            bottom = mRect.bottom + mCornerStrokeWidth / 2;
        }
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);

        right = mRect.right + mCornerStrokeWidth / 2;
        left = right - mCornerWidth;
        if (left <= mRect.left - mCornerStrokeWidth / 2) {
            left = mRect.left - mCornerStrokeWidth / 2;
        }
        top = mRect.top - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        left = mRect.right - mCornerStrokeWidth / 2;
        bottom = top + mCornerWidth;
        if (bottom >= mRect.bottom + mCornerStrokeWidth / 2) {
            bottom = mRect.bottom + mCornerStrokeWidth / 2;
        }
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);

        left = mRect.left - mCornerStrokeWidth / 2;
        right = left + mCornerWidth;
        if (right >= mRect.right + mCornerStrokeWidth / 2) {
            right = mRect.right + mCornerStrokeWidth / 2;
        }
        top = mRect.bottom - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        right = left + mCornerStrokeWidth;
        top = bottom - mCornerWidth;
        if (top <= mRect.top - mCornerStrokeWidth / 2) {
            top = mRect.top - mCornerStrokeWidth / 2;
        }
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);

        right = mRect.right + mCornerStrokeWidth / 2;
        left = right - mCornerWidth;
        if (left <= mRect.left - mCornerStrokeWidth / 2) {
            left = mRect.left - mCornerStrokeWidth / 2;
        }
        top = mRect.bottom - mCornerStrokeWidth / 2;
        bottom = top + mCornerStrokeWidth;
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
        left = mRect.right - mCornerStrokeWidth / 2;
        top = bottom - mCornerWidth;
        if (top <= mRect.top - mCornerStrokeWidth / 2) {
            top = mRect.top - mCornerStrokeWidth / 2;
        }
        rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, mCornerPaint);
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
        String matrixStr = "";
        for (int i = 0; i < matrixValues.length; i++) {
            matrixStr += matrixValues[i] + "  ";
            if (i % 3 == 2) {
                Log.v(TAG, matrixStr);
                matrixStr = "";
            }
        }
        // Extract the scale and translation values. Note, we currently do not handle any other transformations (e.g. skew).
        float scaleX = matrixValues[Matrix.MSCALE_X];
        float scaleY = matrixValues[Matrix.MSCALE_Y];
        float transX = matrixValues[Matrix.MTRANS_X];
        float transY = matrixValues[Matrix.MTRANS_Y];

        final Bitmap originalBitmap = ((BitmapDrawable) drawable).getBitmap();
        int bitmapWidth = originalBitmap.getWidth();
        int bitmapHeight = originalBitmap.getHeight();
        Log.v(TAG, "bitmapWidth = " + bitmapWidth + "; bitmapHeight = " + bitmapHeight);
        Log.v(TAG, "; mRect.left = " + mRect.left + "; mRect.top = " + mRect.top + "; mRect.right = " + mRect.right + "; mRect.bottom = " + mRect.bottom + "; mRect.width = " + getCropWidth() + "; mRect.height = " + getCropHeight());
        Log.v(TAG, "width = " + getWidth() + "; height = " + getHeight());

        // Ensure that the left and top edges are not outside of the ImageView bounds.
        float bitmapLeft = (transX < 0) ? Math.abs(transX) : 0;
        float bitmapTop = (transY < 0) ? Math.abs(transY) : 0;
        ScaleType scaleType = getScaleType();
        if (scaleType == ScaleType.CENTER) {

        } else if (scaleType == ScaleType.CENTER_CROP) {

        } else if (scaleType == ScaleType.CENTER_INSIDE) {

        } else if (scaleType == ScaleType.FIT_CENTER) {

        } else if (scaleType == ScaleType.FIT_START) {

        } else if (scaleType == ScaleType.FIT_END) {

        } else if (scaleType == ScaleType.FIT_XY) {
            scaleX = getWidth() / (float) bitmapWidth;
            scaleY = getHeight() / (float) bitmapHeight;
        }
        float cropX = (bitmapLeft + mRect.left) / scaleX;
        float cropY = (bitmapTop + mRect.top) / scaleY;
        if (getScaleType() == ScaleType.FIT_CENTER) {
            scaleX = (float) getRadio();
            scaleY = (float) getRadio();
            cropX = (mRect.left - (getWidth() - getWidthInFact()) / 2) / scaleX;
            cropY = (mRect.top - (getHeight() - getHeightInFact()) / 2) /scaleY;
        }

        float width = getCropWidth() / scaleX;
        float height = getCropHeight() / scaleY;
        if (cropX < 0) {
            cropX = 0;
        }
        if (cropY < 0) {
            cropY = 0;
        }
        if (width + cropX >= bitmapWidth) {
            width = bitmapWidth - cropX;
        }
        if (height + cropY >= bitmapHeight) {
            height = bitmapHeight - cropY;
        }
        Log.v(TAG, "createBitmap, cropX = " + cropX + "; cropY = " + cropY + "; width = " + width + "; height = " + height);
        Matrix matrix = new Matrix();
//        matrix.setRotate(mRotateDegree);
        return Bitmap.createBitmap(originalBitmap, (int) cropX, (int) cropY, (int) width, (int) height, matrix, false);
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
            if (x < (getWidth() - getWidthInFact()) / 2 || y <  (getHeight() - getHeightInFact()) / 2 || x > (getWidth() + getWidthInFact()) / 2 || y > (getHeight() + getHeightInFact()) / 2) {
                return true;
            }
            if (mIsNearbyCorner) {
                if (mNearByCornerIndex == CORNER_INDEX_LEFT_TOP) {
                    if (mRect.right - x <= mRectStrokeWidth || mRect.bottom - y <= mRectStrokeWidth) {
                        return true;
                    }
                    mRect.left = (int) x;
                    mRect.top = (int) y;
                    invalidate();
                } else if (mNearByCornerIndex == CORNER_INDEX_RIGHT_TOP) {
                    if (x - mRect.left <= mRectStrokeWidth || mRect.bottom - y <= mRectStrokeWidth) {
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
                if (mRect.left + distanceX <= (getWidth() - getWidthInFact()) / 2) {
                    mRect.left = (getWidth() - getWidthInFact()) / 2;
                    mRect.right += distanceX;
                    mRect.top += distanceY;
                    mRect.bottom += distanceY;
                    Log.v(TAG, "mRect = " + mRect);
                } else if (mRect.right + distanceX >= (getWidth() + getWidthInFact()) / 2) {
                    mRect.right = (getWidth() + getWidthInFact()) / 2;
                    mRect.left += distanceX;
                    mRect.top += distanceY;
                    mRect.bottom += distanceY;
                } else if (mRect.top + distanceY <=  (getHeight() - getHeightInFact()) / 2) {
                    mRect.top = (getHeight() - getHeightInFact()) / 2;
                    mRect.left += distanceX;
                    mRect.right += distanceX;
                    mRect.bottom += distanceY;
                } else if (mRect.bottom + distanceY >= (getHeight() + getHeightInFact()) / 2) {
                    mRect.bottom = (getHeight() + getHeightInFact()) / 2;
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