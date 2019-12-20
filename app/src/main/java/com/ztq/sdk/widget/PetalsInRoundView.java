package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ztq.sdk.R;

import java.util.List;

/**
 * Author: ztq
 * Date: 2019/12/20 10:56
 * Description: 花瓣圆形的自定义View，最底层是圆形，上面有一层花瓣
 */
public class PetalsInRoundView extends View {
    private static final String TAG = "noahedu.PetalsInRoundView";

    private Context mContext;
    private Paint mPaint;
    /**高亮的序号*/
    private int mHighlightIndex;
    /**圆形半径(加上圆环的半径长)*/
    private float mCircleRadius;
    /**经过圆心的实心圆弧颜色(非高亮)*/
    private int mNormalFillArcColor;
    /**经过圆心的实心圆弧颜色(高亮)*/
    private int mHighlightFillArcColor;
    /**圆形外边边界stroke宽度*/
    private float mCircleBorderStrokeWidth;
    /**细半径线的宽度*/
    private float mThinRadiusLineWidth;
    /**粗半径线的宽度*/
    private float mThickRadiusLineWidth;
    /**半径线的颜色*/
    private int mRadiusLineColor;
    /**花瓣名字列表*/
    private List<String> mPetalNameList;
    private int mWidth;
    private int mHeight;
    /**每份花瓣占用的角度*/
    private double mEachPetalAngle;
    /**花瓣竖直方向的高占整个圆半径的比例*/
    private float mPetalImageInRadiuRaido = 0.75f;

    public PetalsInRoundView(Context context) {
        this(context, null);
    }

    public PetalsInRoundView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PetalsInRoundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PetalsRoundView);
        mNormalFillArcColor = typedArray.getColor(R.styleable.PetalsRoundView_normalFillArcColor, getResources().getColor(R.color.gray));
        mHighlightFillArcColor = typedArray.getColor(R.styleable.PetalsRoundView_highlightFillArcColor, getResources().getColor(R.color.white));
        mCircleBorderStrokeWidth = typedArray.getDimension(R.styleable.PetalsRoundView_circleBorderStrokeWidth, getResources().getDimension(R.dimen.petals_round_view_circle_border_stroke_width));
        mThinRadiusLineWidth = typedArray.getDimension(R.styleable.PetalsRoundView_thinRadiusLineWidth, getResources().getDimension(R.dimen.petals_round_view_thin_radius_line_width));
        mThickRadiusLineWidth = typedArray.getDimension(R.styleable.PetalsRoundView_thickRadiusLineWidth, getResources().getDimension(R.dimen.petals_round_view_thick_radius_line_width));
        mRadiusLineColor = typedArray.getColor(R.styleable.PetalsRoundView_radiusLineColor, getResources().getColor(R.color.light_gray));
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void setHighlightIndex(int mHighlightIndex) {
        this.mHighlightIndex = mHighlightIndex;
    }

    public void setPetalNameList(List<String> mPetalNameList) {
        this.mPetalNameList = mPetalNameList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCircleRadius = Math.min(mWidth, mHeight) / 2;
        if (mCircleRadius <= 0 || mCircleRadius <= mCircleBorderStrokeWidth) {
            return;
        }
        Log.v(TAG, "mWidth = " + mWidth + "; mHeight = " + mHeight + "");
        drawCirclePart(canvas);
        drawLinePart(canvas);
        drawPetalsPart(canvas);
    }

    /**
     * 画圆形区域
     * @param canvas
     */
    private void drawCirclePart(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mNormalFillArcColor);
        canvas.drawCircle(mCircleRadius, mCircleRadius, mCircleRadius - mCircleBorderStrokeWidth, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mRadiusLineColor);
        mPaint.setStrokeWidth(mCircleBorderStrokeWidth);
        canvas.drawCircle(mCircleRadius, mCircleRadius, mCircleRadius - mCircleBorderStrokeWidth, mPaint);
    }

    /**
     * 画园内的直线
     * @param canvas
     */
    private void drawLinePart(Canvas canvas) {
        if (mPetalNameList == null || mPetalNameList.size() == 0) {
            return;
        }
        mPaint.setColor(mRadiusLineColor);
        mPaint.setStrokeWidth(mThickRadiusLineWidth);
        mEachPetalAngle = 2 * Math.PI / mPetalNameList.size();
        //先画粗线
        for(int i = 0; i < mPetalNameList.size(); i++) {
            double angle = i * mEachPetalAngle;
            Log.v(TAG, "i = " + i + "; angle = " + angle + "; sin = " + Math.sin(angle) + "; cos = " + (-Math.cos(angle)));
            double targetX = mCircleRadius + (mCircleRadius - mCircleBorderStrokeWidth) * Math.sin(angle);
            double targetY = mCircleRadius + (mCircleRadius - mCircleBorderStrokeWidth) * (- Math.cos(angle));
            canvas.drawLine(mCircleRadius, mCircleRadius, (float)targetX, (float)targetY, mPaint);
        }
    }

    /**
     * 画花瓣，每个花瓣分成两半，分两次画，注释下：假如当前高亮的花瓣序号为0，则先画序号为1 的左半片，
     * 再画高亮花瓣的右半片，而后高亮花瓣的左半片，依次以逆时针的顺序每次画半片花瓣，最后画序号为1的右半片
     * @param canvas
     */
    private void drawPetalsPart(Canvas canvas) {
        int firstDrawIndex = mHighlightIndex < mPetalNameList.size() - 1 ? mHighlightIndex + 1 : 0;
        drawPetalsPart(canvas, true, true, firstDrawIndex);
        drawPetalsPart(canvas, false, false, mHighlightIndex);
        drawPetalsPart(canvas, true, false, mHighlightIndex);
        for(int i = (mHighlightIndex - 1 + mPetalNameList.size()) % mPetalNameList.size(); i > firstDrawIndex; i--) {
            drawPetalsPart(canvas, false, true, i);
            drawPetalsPart(canvas, true, true, i);
        }
        drawPetalsPart(canvas, false, true, firstDrawIndex);
    }

    private void drawPetalsPart(Canvas canvas, boolean isLeftPetalPart, boolean isNormalPetal, int index) {
        if (mPetalNameList == null || mPetalNameList.size() == 0 || mEachPetalAngle == 0) {
            return;
        }
        int resId = R.drawable.ic_normal_petal_left;
        if (isLeftPetalPart && isNormalPetal) {
            resId = R.drawable.ic_normal_petal_left;
        } else if (!isLeftPetalPart && isNormalPetal) {
            resId = R.drawable.ic_normal_petal_right;
        } else if (isLeftPetalPart && !isNormalPetal) {
            resId = R.drawable.ic_highlight_petal_left;
        } else if (!isLeftPetalPart && !isNormalPetal) {
            resId = R.drawable.ic_highlight_petal_right;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float raido = (float)(mCircleRadius * mPetalImageInRadiuRaido) / height;
        float newWidth = width * raido;
        float newHeight = height * raido;
        Bitmap targetBitmap = Bitmap.createScaledBitmap(bitmap, (int)newWidth, (int)newHeight, false);
        if (targetBitmap != null) {
            bitmap.recycle();
        }
        canvas.save();
        float translateX = mCircleRadius - newWidth;
        float rotateX = newWidth;
        if (!isLeftPetalPart) {
            translateX = mCircleRadius;
            rotateX = 0;
        }
        canvas.translate(translateX,  mCircleRadius - newHeight);
        int degree = (int)((index + 0.5) * mEachPetalAngle * 360 / (2 * Math.PI));
        //旋转的角度是以度为单位
        canvas.rotate(degree, rotateX, newHeight);

        Log.v(TAG, "degree = " + ((index + 0.5) * mEachPetalAngle) * 360 / (2 * Math.PI) + "; " + width * raido + "; " + height * raido);
        canvas.drawBitmap(targetBitmap, 0, 0, null);
        canvas.restore();
    }
}