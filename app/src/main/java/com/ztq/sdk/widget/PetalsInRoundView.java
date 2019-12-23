package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
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
    /**每个小扇形内每行显示的字符数*/
    private static final int SECTOR_TEXT_MAX_LEN_EACH_LINE = 2;
    /**每个花瓣内每行显示的字符数*/
    private static final int PETAL_AREA_MAX_LEN_EACH_LINE = 2;
    /**每个花瓣内最多显示的行数*/
    private static final int PETAL_AREA_TEXT_MAX_LINES = 2;
    private static final float DISTANCE_RAIDO_OF_RECT_CENTER_POINT_TO_SHARP_CORNER = 0.18f;
    private float mDistanceOfRectCenterPointToCircleCenter;
    private Context mContext;
    private Paint mPaint;
    private TextPaint mTextPaint;
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
    private float mRatioOfPetalImgHeightToCircle = 0.75f;
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
    /**花瓣内文字大小*/
    private float mPetalTextSize;
    /**花瓣内文字颜色(非高亮状态)*/
    private int mPetalNormalTextColor;
    /**花瓣内文字颜色(高亮状态)*/
    private int mPetalHighlightTextColor;
    /**花瓣内的文本一行最多显示多少个字*/
    private int mPetalAreaMaxLenEachLine;
    /**花瓣内的文字最多显示多少行（默认两行）*/
    private int mPetalAreaTextMaxLines;

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
        mRatioOfPetalImgHeightToCircle = typedArray.getFloat(R.styleable.PetalsRoundView_ratioOfPetalImgHeightToCircle, 0.75f);
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
        mPetalTextSize = typedArray.getDimension(R.styleable.PetalsRoundView_petalTextSize, getResources().getDimension(R.dimen.petals_round_view_petal_text_size));
        mPetalNormalTextColor = typedArray.getColor(R.styleable.PetalsRoundView_petalNormalTextColor, getResources().getColor(R.color.white));
        mPetalHighlightTextColor = typedArray.getColor(R.styleable.PetalsRoundView_petalHighlightTextColor, getResources().getColor(R.color.black));
        mPetalAreaMaxLenEachLine = typedArray.getInt(R.styleable.PetalsRoundView_petalAreaMaxLenEachLine, PETAL_AREA_MAX_LEN_EACH_LINE);
        mPetalAreaTextMaxLines = typedArray.getInt(R.styleable.PetalsRoundView_petalAreaTextMaxLines, PETAL_AREA_TEXT_MAX_LINES);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
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
        drawCirclePart(canvas);
        drawLinePartAndSectorText(canvas);
        drawPetalsPart(canvas);
        drawPetalsText(canvas);
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
     * 画园内的直线和扇形文字
     * @param canvas
     */
    private void drawLinePartAndSectorText(Canvas canvas) {
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null || mPetalsInfo.getPetalList().size() == 0) {
            return;
        }
        mPaint.setColor(mRadiusLineColor);
        mEachPetalAngle = 2 * Math.PI / mPetalsInfo.getPetalList().size();
        float delta1 = (float)(0.1 * (mCircleRadius - mCircleBorderStrokeWidth));
        float delta = mCircleBorderStrokeWidth + delta1;
        RectF oval = new RectF(delta, delta,  2 * mCircleRadius - delta, 2 * mCircleRadius - delta);
        //先画粗线
        int petalsSize = mPetalsInfo.getPetalList().size();
        for(int i = 0; i < petalsSize; i++) {
            double angle = i * mEachPetalAngle;
            double targetX = mCircleRadius + (mCircleRadius - mCircleBorderStrokeWidth) * Math.sin(angle);
            double targetY = mCircleRadius + (mCircleRadius - mCircleBorderStrokeWidth) * (-Math.cos(angle));
            mPaint.setStrokeWidth(mThickRadiusLineWidth);
            canvas.drawLine(mCircleRadius, mCircleRadius, (float)targetX, (float)targetY, mPaint);

            PetalsInfo.PetalEntity entity  = mPetalsInfo.getPetalList().get(i);
            //画出圆弧细线（即花瓣下面有几项）
            if (entity != null) {
                List<String> childNameList = entity.getChildList();
                if (childNameList != null && childNameList.size() != 0) {
                    int childSize = childNameList.size();
                    double eachChildEachPetalAngle = mEachPetalAngle / childSize;
                    for(int j = 0; j < childSize; j++) {
                        double childAngle = angle + (j + 1) * eachChildEachPetalAngle;
                        String childName = childNameList.get(j);
                        mTextPaint.setTextSize(mOuterSectorTextSize);
                        mTextPaint.setColor(mOuterSectorTextColor);

                        int textLines = childName.length() % SECTOR_TEXT_MAX_LEN_EACH_LINE == 0 ? childName.length() / SECTOR_TEXT_MAX_LEN_EACH_LINE : childName.length() / SECTOR_TEXT_MAX_LEN_EACH_LINE + 1;
                        for(int k = 0; k < textLines; k++ ) {
                            String subName = "";                      // 每个小扇形内每行显示的字符
                            if (k == textLines - 1) {
                                subName = childName.substring(k * SECTOR_TEXT_MAX_LEN_EACH_LINE, childName.length());
                            } else {
                                subName = childName.substring(k * SECTOR_TEXT_MAX_LEN_EACH_LINE, (k + 1) * SECTOR_TEXT_MAX_LEN_EACH_LINE);
                            }
                            float subWidth = mTextPaint.measureText(subName);
                            float subHeight = mTextPaint.getFontSpacing();
                            float deltaHeight = delta1 / 2 + (k + 1 / 2) * subHeight;    // 每行文字中心离外层的圆边界高度差（不包含圆形外边边界strokestroke）
                            double radio = subWidth / ((mCircleRadius - mCircleBorderStrokeWidth - deltaHeight) * 2 * Math.PI / (petalsSize * childSize));
                            float deltaAngle = 0;                    // 每行文字与相应扇形缩进的角度
                            if (radio < 1 && radio > 0) {
                                deltaAngle = (float)((1 - radio) / 2 * Math.toDegrees(eachChildEachPetalAngle));
                            }
                            Path path = new Path();
                            float startAngle = (float)(Math.toDegrees(childAngle - eachChildEachPetalAngle) - Math.toDegrees(Math.PI / 2)) + deltaAngle;
                            float sweepAngle = (float)Math.toDegrees(eachChildEachPetalAngle);
                            path.addArc(oval, startAngle, sweepAngle);
                            Log.v(TAG, "startAngle = " + startAngle + "; sweepAngle = " + sweepAngle + "; i = " + i + "; j = " + j + "; k = " + k + "; subName = " + subName);
                            canvas.drawTextOnPath(subName, path, 0, deltaHeight - 1 / 2 * subHeight, mTextPaint);
                        }
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
        if (mHighlightIndex >= mPetalsInfo.getPetalList().size() || mHighlightIndex < 0) {
            mHighlightIndex = 0;
        }
        int firstDrawIndex = mHighlightIndex <= mPetalsInfo.getPetalList().size() - 1 ? mHighlightIndex + 1 : 0;
        drawPetalsPart(canvas, true, true, firstDrawIndex);
        drawPetalsPart(canvas, false, false, mHighlightIndex);
        drawPetalsPart(canvas, true, false, mHighlightIndex);
        for(int i = mHighlightIndex - 1 + mPetalsInfo.getPetalList().size(); i > firstDrawIndex; i--) {
            int index = i % mPetalsInfo.getPetalList().size();
            drawPetalsPart(canvas, false, true, index);
            drawPetalsPart(canvas, true, true, index);
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
        float raido = (float)(mCircleRadius * mRatioOfPetalImgHeightToCircle) / height;
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
        int degree = (int)Math.toDegrees((index + 0.5) * mEachPetalAngle);
        //旋转的角度是以度为单位
        canvas.rotate(degree, rotateX, newHeight);

        Log.v(TAG, "degree = " + (Math.toDegrees((index + 0.5) * mEachPetalAngle)) + "; " + width * raido + "; " + height * raido);
        canvas.drawBitmap(targetBitmap, 0, 0, null);
        canvas.restore();
    }

    /**
     * 画花瓣内的文字，这里利用的思路是不管文字是占用一行，还是占用两行，
     * 文字所在的矩形的中心点我们暂定是在花瓣的尖角与圆心的连线上，且离尖角的距离是一定值，这里设置为0.15 * mCircleRadius
     * @param canvas
     */
    private void drawPetalsText(Canvas canvas) {
        if (mCircleRadius <= 0 || mPetalsInfo == null || mPetalsInfo.getPetalList() == null || mPetalsInfo.getPetalList().size() == 0) {
            return;
        }
        List<PetalsInfo.PetalEntity> list = mPetalsInfo.getPetalList();
        mDistanceOfRectCenterPointToCircleCenter = (mRatioOfPetalImgHeightToCircle - DISTANCE_RAIDO_OF_RECT_CENTER_POINT_TO_SHARP_CORNER) * mCircleRadius;
        mEachPetalAngle = 2 * Math.PI / list.size();
        for(int i = 0; i < list.size(); i++) {
            PetalsInfo.PetalEntity entity = list.get(i);
            if (entity == null || Utils.isNullOrNil(entity.getName())) {
                continue;
            }
            String name = entity.getName();
            double sharpCornerAngle = (i + 1f / 2) * mEachPetalAngle ;
            Log.v(TAG, "sharpCornerAngle = " + sharpCornerAngle + "; " + Math.toDegrees(sharpCornerAngle));
            int lines = name.length() % mPetalAreaMaxLenEachLine == 0 ? name.length() / mPetalAreaMaxLenEachLine : name.length() / mPetalAreaMaxLenEachLine + 1;
            double centerPointX = mCircleRadius + mDistanceOfRectCenterPointToCircleCenter * Math.sin(sharpCornerAngle);
            double centerPointY = mCircleRadius - mDistanceOfRectCenterPointToCircleCenter * Math.cos(sharpCornerAngle);
            mTextPaint.setTextSize(mPetalTextSize);
            if (i == mHighlightIndex) {
                mTextPaint.setColor(mPetalHighlightTextColor);
            } else {
                mTextPaint.setColor(mPetalNormalTextColor);
            }
            float fontHeight = mTextPaint.getFontSpacing();
            for(int j = 0; j < lines; j++) {
                if (j > (mPetalAreaTextMaxLines - 1)) {        //加一个限制:最多显示mPetalAreaTextMaxLines行
                    break;
                }
                String childName = "";
                if (j == lines - 1) {
                    childName = name.substring(mPetalAreaMaxLenEachLine * j, name.length());
                } else {
                    childName = name.substring(mPetalAreaMaxLenEachLine * j, mPetalAreaMaxLenEachLine * (j + 1));
                }
                float width = mTextPaint.measureText(childName);
                canvas.drawText(childName, (float)(centerPointX - width / 2), (float)(centerPointY + (j + 1 - lines / 2f) * fontHeight - mTextPaint.getFontMetrics().bottom), mTextPaint);
            }
        }
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

            mTextPaint.setTextSize(mInnerCircleTextSize);
            mTextPaint.setColor(mInnerCircleTextColor);
            String chileName = "";
            for (int i = 0; i < textline; i++) {
                if (i == textline - 1) {
                    chileName = name.substring(mInnerCircleMaxLenEachLine * i, name.length());
                } else {
                    chileName += name.substring(mInnerCircleMaxLenEachLine * i, mInnerCircleMaxLenEachLine * (i + 1));
                }
                float textWidth = mTextPaint.measureText(chileName);
                float textHeight = mTextPaint.getFontSpacing();
                float x = mCircleRadius - textWidth / 2;
                float y = mCircleRadius + (i + 1 - textline / 2) * textHeight - mTextPaint.getFontMetrics().bottom;
                canvas.drawText(chileName, x, y, mTextPaint);
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
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null) {
            return false;
        }
        int action = event.getAction();
        float touchX = event.getX();
        float touchY = event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            if (judgePointIsInCircle(touchX, touchY, mInnerCircleRadius - mInnerRingWidth)) {
                if (mOnInnerCircleClickListener != null) {
                    mOnInnerCircleClickListener.onClick(this);
                }
            } else {
                if (mPetalsInfo.getPetalList().size() != 0) {
                    for(int i = 0; i < mPetalsInfo.getPetalList().size(); i++) {
                        PetalsInfo.PetalEntity entity = mPetalsInfo.getPetalList().get(i);
                        if (entity != null && entity.getChildList() != null) {
                            for(int j = 0; j < entity.getChildList().size(); j++){
                                if (isBelongToCertainArea(touchX, touchY, i, j)) {
                                    if (mSectorClickListener != null) {
                                        mSectorClickListener.onClick(i, j);
                                    }
                                    break;
                                }
                            }
                            String name = entity.getName();
                            if (Utils.isNullOrNil(name)) {
                                continue;
                            }
                            if (isBelongToCertainRect(touchX, touchY, i, name)) {
                                if (mPetalClickListener != null) {
                                    mPetalClickListener.onClick(i);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 判断点(x,y)是否在特定矩形内，矩形规则如下：以花瓣文字矩形的中心点为中心点，左右各扩展PETAL_AREA_MAX_LEN_EACH_LINE/2个中文字符，上下各扩展 mPetalAreaTextMaxLines/2 中文字符
     * @param x
     * @param y
     * @param petalName
     * @return
     */
    private boolean isBelongToCertainRect(float x, float y, int index, String petalName) {
        if (Utils.isNullOrNil(petalName)) {
            return false;
        }
        double sharpCornerAngle = (index + 1f / 2) * mEachPetalAngle ;
        Log.v(TAG, "sharpCornerAngle = " + sharpCornerAngle + "; " + Math.toDegrees(sharpCornerAngle));
        float centerPointX = (float)(mCircleRadius + mDistanceOfRectCenterPointToCircleCenter * Math.sin(sharpCornerAngle));
        float centerPointY = (float)(mCircleRadius - mDistanceOfRectCenterPointToCircleCenter * Math.cos(sharpCornerAngle));
       mTextPaint.setTextSize(mPetalTextSize);
        float fontHeight = mTextPaint.getFontSpacing();
        String str = "";
        for(int i = 0; i < mPetalAreaMaxLenEachLine; i++) {
            str += "我";
        }
        float width = mTextPaint.measureText(str);
        RectF rect = new RectF(centerPointX - width / 2, centerPointY - mPetalAreaTextMaxLines / 2f * fontHeight, centerPointX + width / 2, centerPointY + mPetalAreaTextMaxLines / 2f * fontHeight);
        return rect.contains(x, y);
    }

    /**
     * 判断点（x,y)是否属于特定区域内，特定区域为：序号为groupIndex的大扇形内的序号为childIndex的子扇形内，
     * 而且在半径为mCircleRadius-mCircleBorderStrokeWidth、半径为mCircleRadius * ratioOfPetalImgHeightToCircle两个圆包围的圆环内
     * @return
     */
    private boolean isBelongToCertainArea(float x, float y, int groupIndex, int childIndex) {
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null || mPetalsInfo.getPetalList().size() == 0) {
            return false;
        }
        if (groupIndex >= mPetalsInfo.getPetalList().size()) {
            return false;
        }
        PetalsInfo.PetalEntity entity = mPetalsInfo.getPetalList().get(groupIndex);
        if (entity == null || entity.getChildList() == null || childIndex >= entity.getChildList().size()) {
            return false;
        }

        boolean result = judgePointIsInCircle(x, y, mCircleRadius-mCircleBorderStrokeWidth);
        result = result && !judgePointIsInCircle(x, y, mCircleRadius * mRatioOfPetalImgHeightToCircle);
        double angle = getAngle(x, y);
        double startAngle = Math.toDegrees(groupIndex * mEachPetalAngle + childIndex * mEachPetalAngle / entity.getChildList().size());
        double endAngle = Math.toDegrees(groupIndex * mEachPetalAngle + (childIndex + 1) * mEachPetalAngle / entity.getChildList().size());
        Log.v(TAG, "result angle = " + angle + "; startAngle = " + startAngle + "; endAngle = " + endAngle);
        result = result && (angle >= startAngle && angle <= endAngle);
        return result;
    }

    /**
     * 获取角度，规则如下：以（mCircleRadius, mCircleRadius）为原点，原点指向（mCircleRadius，0）的线段为x正轴
     * 原点指向（2*mCircleRadius，mCircleRadius）为y正轴，获取原点指向(x,y)与x正轴的夹角
     * @param x
     * @param y
     * @return
     */
    private double getAngle(float x, float y) {
        double result = 0;
        if (x >= mCircleRadius && y <= mCircleRadius) {          // 夹角在0-90度之间
            result = Math.toDegrees(Math.asin((x - mCircleRadius) / Math.sqrt(Math.pow(x - mCircleRadius, 2) + Math.pow(mCircleRadius - y, 2))));
        } else if (x >= mCircleRadius && y >= mCircleRadius) {   // 夹角在90-180度之间
            result = Math.toDegrees(Math.PI / 2) + Math.toDegrees(Math.asin((y - mCircleRadius) / Math.sqrt(Math.pow(x - mCircleRadius, 2) + Math.pow(y - mCircleRadius, 2))));
        } else if (x <= mCircleRadius && y >= mCircleRadius) {   // 夹角在180-270度之间
            result = Math.toDegrees(Math.PI) + Math.toDegrees(Math.asin((mCircleRadius - x) / Math.sqrt(Math.pow(mCircleRadius - x, 2) + Math.pow(mCircleRadius - y, 2))));
        } else if (x <= mCircleRadius && y <= mCircleRadius) {   // 夹角在270-360度之间
            result = Math.toDegrees(3 * Math.PI / 2) + Math.toDegrees(Math.asin((mCircleRadius - y) / Math.sqrt(Math.pow(mCircleRadius - x, 2) + Math.pow(mCircleRadius - y, 2))));
        }
        return result;
    }

    /**
     * 判断点（x,y)是否在圆心（mCircleRadius，mCircleRadius），半径为radius的圆内
     * @param x
     * @param y
     * @param radius
     * @return
     */
    private boolean judgePointIsInCircle(float x, float y, float radius) {
        if (x < 0 || y < 0 || radius <= 0) {
            return false;
        }
        return Math.sqrt(Math.pow(x - mCircleRadius, 2) + Math.pow(y - mCircleRadius, 2)) < radius;
    }

    /**
     * 点击花瓣的监听
     */
    public interface PetalClickListener{
        public void onClick(int index);
    }

    /**
     * 点击外围扇形的点击监听
     */
    public interface SectorClickListener {
        public void onClick(int groupIndex, int childIndex);
    }
}