package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ztq.sdk.R;
import com.ztq.sdk.entity.PetalsInfo;
import com.ztq.sdk.utils.Utils;

import java.util.List;

/**
 * Author: ztq
 * Date: 2019/12/20 10:56
 * Description: 花瓣圆形的自定义View，最底层是圆形，上面有一层花瓣
 */
public class PetalsInRoundView extends View {
    private static final String TAG = "noahedu.PetalsInRoundView";
    private static final int INNER_CIRCLE_MAX_LEN_EACH_LINE = 3;

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
    private int mWidth;
    private int mHeight;
    /**每份花瓣占用的角度*/
    private double mEachPetalAngle;
    /**花瓣竖直方向的高占整个圆半径的比例*/
    private float ratioOfPetalImgHeightToCircle = 0.75f;
    /**内圈圆的半径大小（包含圆环）*/
    private float mInnerCircleRadius;
    /**内圈圆环的宽度*/
    private float mInnerRingWidth;
    /**内圈圆环的颜色*/
    private int mInnerRingColor;
    /**内圈圆的颜色（不包含圆环）*/
    private int mInnerCircleExceptRingColor;
    /**内圈圆的文字大小*/
    private float mInnerCircleTextSize;
    /**内圈圆的文本颜色*/
    private int mInnerCircleTextColor;
    /**外圈扇形的文字大小*/
    private float mOuterSectorTextSize;
    /**外圈圆的文字颜色*/
    private int mOuterSectorTextColor;
    /**内圈圆内一行最多显示多少个字*/
    private int mInnerCircleMaxLenEachLine;

    /**点击内圈圆的监听响应*/
    private OnClickListener mOnInnerCircleClickListener;
    /**点击花瓣的监听*/
    private PetalClickListener mPetalClickListener;
    /**点击外围扇形的监听*/
    private SectorClickListener mSectorClickListener;
    /**花瓣信息（包含花瓣的列表信息）*/
    private PetalsInfo mPetalsInfo;

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
        ratioOfPetalImgHeightToCircle = typedArray.getFloat(R.styleable.PetalsRoundView_ratioOfPetalImgHeightToCircle, 0.75f);
        mCircleBorderStrokeWidth = typedArray.getDimension(R.styleable.PetalsRoundView_circleBorderStrokeWidth, getResources().getDimension(R.dimen.petals_round_view_circle_border_stroke_width));
        mThinRadiusLineWidth = typedArray.getDimension(R.styleable.PetalsRoundView_thinRadiusLineWidth, getResources().getDimension(R.dimen.petals_round_view_thin_radius_line_width));
        mThickRadiusLineWidth = typedArray.getDimension(R.styleable.PetalsRoundView_thickRadiusLineWidth, getResources().getDimension(R.dimen.petals_round_view_thick_radius_line_width));
        mRadiusLineColor = typedArray.getColor(R.styleable.PetalsRoundView_radiusLineColor, getResources().getColor(R.color.light_gray));
        mInnerCircleRadius = typedArray.getDimension(R.styleable.PetalsRoundView_innerCircleRadius, getResources().getDimension(R.dimen.petals_round_view_inner_circle_radius));
        mInnerRingWidth = typedArray.getDimension(R.styleable.PetalsRoundView_innerRingWidth, getResources().getDimension(R.dimen.petals_round_view_inner_ring_width));
        mInnerRingColor = typedArray.getColor(R.styleable.PetalsRoundView_innerRingColor, getResources().getColor(R.color.light_gray_ring));
        mInnerCircleExceptRingColor = typedArray.getColor(R.styleable.PetalsRoundView_innerCircleExceptRingColor, getResources().getColor(R.color.middle_black));
        mInnerCircleTextSize = typedArray.getDimension(R.styleable.PetalsRoundView_innerCircleTextSize, getResources().getDimension(R.dimen.petals_round_view_inner_circle_text_size));
        mInnerCircleTextColor = typedArray.getColor(R.styleable.PetalsRoundView_innerCircleTextColor, getResources().getColor(R.color.white));
        mOuterSectorTextSize = typedArray.getDimension(R.styleable.PetalsRoundView_outerSectorTextSize, getResources().getDimension(R.dimen.petals_round_view_outer_sector_text_size));
        mOuterSectorTextColor = typedArray.getColor(R.styleable.PetalsRoundView_outerSectorTextColor, getResources().getColor(R.color.light_gray));
        mInnerCircleMaxLenEachLine = typedArray.getInt(R.styleable.PetalsRoundView_innerCircleMaxLenEachLine, INNER_CIRCLE_MAX_LEN_EACH_LINE);
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

    public void setPetalsInfo(PetalsInfo mPetalsInfo) {
        this.mPetalsInfo = mPetalsInfo;
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
        drawInnerCircle(canvas);
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
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null || mPetalsInfo.getPetalList().size() == 0) {
            return;
        }
        mPaint.setColor(mRadiusLineColor);
        mEachPetalAngle = 2 * Math.PI / mPetalsInfo.getPetalList().size();
        float delta = (float)(mCircleBorderStrokeWidth + 0.1 * (mCircleRadius - mCircleBorderStrokeWidth));
        RectF oval = new RectF(0, 0,  2 * mCircleRadius, 2 * mCircleRadius);
        //先画粗线
        for(int i = 0; i < mPetalsInfo.getPetalList().size(); i++) {
            double angle = i * mEachPetalAngle;
            Log.v(TAG, "i = " + i + "; angle = " + angle + "; sin = " + Math.sin(angle) + "; cos = " + (-Math.cos(angle)));
            double targetX = mCircleRadius + (mCircleRadius - mCircleBorderStrokeWidth) * Math.sin(angle);
            double targetY = mCircleRadius + (mCircleRadius - mCircleBorderStrokeWidth) * (-Math.cos(angle));
            mPaint.setStrokeWidth(mThickRadiusLineWidth);
            canvas.drawLine(mCircleRadius, mCircleRadius, (float)targetX, (float)targetY, mPaint);

            PetalsInfo.PetalEntity entity  = mPetalsInfo.getPetalList().get(i);
            //画出圆弧细线（即花瓣下面有几项）
            if (entity != null) {
                List<String> childNameList = entity.getChildList();
                if (childNameList != null && childNameList.size() != 0) {
                    Log.v(TAG, "childNameList size = " + childNameList.size());
                    for(int j = 0; j < childNameList.size(); j++) {
                        double eachChildEachPetalAngle = mEachPetalAngle / childNameList.size();
                        double childAngle = angle + (j + 1) * eachChildEachPetalAngle;
                        String childName = childNameList.get(j);
                        Path path = new Path();

                        mPaint.setTextSize(mOuterSectorTextSize);
                        mPaint.setColor(mOuterSectorTextColor);
                        float startAngle = (float)((childAngle - eachChildEachPetalAngle) * 360 / (2 * Math.PI)) - 90 + 5;
                        float sweepAngle = (float)(eachChildEachPetalAngle * 360 / (2 * Math.PI));
                        Log.v(TAG, "startAngle = " + startAngle + "; sweepAngle = " + sweepAngle);
                        path.addArc(oval, startAngle, sweepAngle);

                        canvas.drawTextOnPath(childName, path, 0, 50, mPaint);
                        if (j == childNameList.size() - 1) {   // 最后一个细线不用画
                            continue;
                        }

                        targetX = mCircleRadius + (mCircleRadius - mCircleBorderStrokeWidth) * Math.sin(childAngle);
                        targetY = mCircleRadius + (mCircleRadius - mCircleBorderStrokeWidth) * (-Math.cos(childAngle));
                        mPaint.setColor(mRadiusLineColor);
                        mPaint.setStrokeWidth(mThinRadiusLineWidth);
                        canvas.drawLine(mCircleRadius, mCircleRadius, (float)targetX, (float)targetY, mPaint);
                    }
                }
            }
        }
    }

    /**
     * 画花瓣，每个花瓣分成两半，分两次画，注释下：假如当前高亮的花瓣序号为0，则先画序号为1 的左半片，
     * 再画高亮花瓣的右半片，而后高亮花瓣的左半片，依次以逆时针的顺序每次画半片花瓣，最后画序号为1的右半片
     * @param canvas
     */
    private void drawPetalsPart(Canvas canvas) {
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null || mPetalsInfo.getPetalList().size() == 0) {
            return;
        }
        int firstDrawIndex = mHighlightIndex < mPetalsInfo.getPetalList().size() - 1 ? mHighlightIndex + 1 : 0;
        drawPetalsPart(canvas, true, true, firstDrawIndex);
        drawPetalsPart(canvas, false, false, mHighlightIndex);
        drawPetalsPart(canvas, true, false, mHighlightIndex);
        for(int i = (mHighlightIndex - 1 + mPetalsInfo.getPetalList().size()) % mPetalsInfo.getPetalList().size(); i > firstDrawIndex; i--) {
            drawPetalsPart(canvas, false, true, i);
            drawPetalsPart(canvas, true, true, i);
        }
        drawPetalsPart(canvas, false, true, firstDrawIndex);
    }

    private void drawPetalsPart(Canvas canvas, boolean isLeftPetalPart, boolean isNormalPetal, int index) {
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null || mPetalsInfo.getPetalList().size() == 0) {
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
        float raido = (float)(mCircleRadius * ratioOfPetalImgHeightToCircle) / height;
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

    /**
     * 画内部圆
     * @param canvas
     */
    private void drawInnerCircle(Canvas canvas) {
        mPaint.setColor(mInnerRingColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mInnerRingWidth);
        canvas.drawCircle(mCircleRadius, mCircleRadius, mInnerCircleRadius - mInnerRingWidth, mPaint);

        mPaint.setColor(mInnerCircleExceptRingColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCircleRadius, mCircleRadius, mInnerCircleRadius - mInnerRingWidth, mPaint);

        if (mPetalsInfo != null && !Utils.isNullOrNil(mPetalsInfo.getName())) {
            String name = mPetalsInfo.getName();
            if (mInnerCircleMaxLenEachLine <= 0) {
                mInnerCircleMaxLenEachLine = INNER_CIRCLE_MAX_LEN_EACH_LINE;
            }
            //是否整除
            boolean isDivisible = name.length() % mInnerCircleMaxLenEachLine == 0;
            int textline = isDivisible ? name.length() / mInnerCircleMaxLenEachLine : name.length() / mInnerCircleMaxLenEachLine + 1;   // 文字行数
            Log.v(TAG, "textline = " + textline + "; name = " + name.length());

            mPaint.setTextSize(mInnerCircleTextSize);
            mPaint.setColor(mInnerCircleTextColor);
            String chileName = "";
            for (int i = 0; i < textline; i++) {
                if (i == textline - 1) {
                    chileName = name.substring(mInnerCircleMaxLenEachLine * i, name.length());
                } else {
                    chileName += name.substring(mInnerCircleMaxLenEachLine * i, mInnerCircleMaxLenEachLine * (i + 1));
                }
                float textWidth = mPaint.measureText(chileName);
                float textHeight = mPaint.getFontSpacing();
                float x = mCircleRadius - textWidth / 2;
                float y = mCircleRadius + (i + 1 - textline / 2) * textHeight - 10;
                Log.v(TAG, "textWidth = " + textWidth + "; textHeight = " + textHeight + "; name = " + name + "; textline = " + textline + "; " + y);

                canvas.drawText(chileName, x, y, mPaint);
            }
        }
    }

    public void setOnInnerCircleClickListener(OnClickListener mOnInnerCircleClickListener) {
        this.mOnInnerCircleClickListener = mOnInnerCircleClickListener;
    }

    public void setPetalClickListener(PetalClickListener mPetalClickListener) {
        this.mPetalClickListener = mPetalClickListener;
    }

    public void setSectorClickListener(SectorClickListener mSectorClickListener) {
        this.mSectorClickListener = mSectorClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Log.v(TAG, "onTouchEvent, action = " + action);
        float touchX = event.getX();
        float touchY = event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            Log.v(TAG, "judgePointIsInInnerCircle = " + judgePointIsInInnerCircle(touchX, touchY) + "; touchX = " + touchX + "; touchY = " + touchY);
            if (judgePointIsInInnerCircle(touchX, touchY)) {
                if (mOnInnerCircleClickListener != null) {
                    mOnInnerCircleClickListener.onClick(this);
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private boolean judgePointIsInInnerCircle(float x, float y) {
        if (x < 0 || y < 0) {
            return false;
        }
        return Math.sqrt(Math.pow(x - mCircleRadius, 2) + Math.sqrt(Math.pow(y - mCircleRadius, 2))) <= (mInnerCircleRadius - mInnerRingWidth);
    }

    /**
     * 点击花瓣的监听
     */
    public interface PetalClickListener{
        public void onClick(View view, int index);
    }

    /**
     * 点击外围扇形的点击监听
     */
    public interface SectorClickListener {
        public void onClick(View view, int groupIndex, int childIndex);
    }
}