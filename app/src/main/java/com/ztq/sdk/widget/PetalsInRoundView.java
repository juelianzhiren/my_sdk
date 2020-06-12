package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewConfiguration;

import com.ztq.sdk.R;
import com.ztq.sdk.constant.Constants;
import com.ztq.sdk.entity.PetalsInfo;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;

import java.util.ArrayList;
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
    /**每片花瓣文字的中心点离花瓣尖角的距离与整个圆半径的比例*/
    private static final float DISTANCE_RAIDO_OF_RECT_CENTER_POINT_TO_SHARP_CORNER = 0.18f;
    /**每片花瓣底部文字所在的圆距离内层圆边界的距离与整个圆半径的比例*/
    private static final float DISTANCE_RAIDO_OF_PETAL_BOTTOM_TEXT_TO_INNER_CIRCLE_CORNER = 0.05f;
    private static final float RATIO_OF_SECTOR_TEXT_TOP_TO_CIRCEL_BORDER = 0.07f;
    private float mDistanceOfRectCenterPointToCircleCenter;
    private Context mContext;
    private Paint mPaint;
    private TextPaint mTextPaint;
    /**高亮的序号*/
    private int mHighlightGroupIndex = -1;
    private static final int HIGHLIGHT_CHILD_INDEX_NO_VALUE = -1;
    private List<Integer> mHighlightChildIndexList;
    /**圆形半径(加上圆环的半径长)*/
    private float mCircleRadius;
    /**经过圆心的实心扇形颜色(非高亮)*/
    private int mNormalFillSectorColor;
    /**经过圆心的实心扇形点击时的颜色(非高亮)*/
    private int mNormalFillSectorColorClick;
    /**经过圆心的实心扇形颜色(高亮)*/
    private int mHighlightFillSectorColor;
    /**经过圆心的实心扇形颜色(高亮)*/
    private int mHighlightFillSectorColorClick;
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
    /**外圈扇形的文字颜色*/
    private int mOuterSectorTextColor;
    /**外圈高亮扇形的文字颜色*/
    private int mOuterSectorHighlightTextColor;
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
    /**花瓣底部的文字大小*/
    private float mPetalAreaBottomTextSize;
    /**花瓣底部的文字颜色*/
    private int mPetalAreaBottomTextColor;
    private float mDistanceOfPetalBottomTextToInnerCircleCorner;
    /**扇形文字顶部距离圆边界与圆半径的比例（不包含stroke区域）*/
    private float mRatioOfSectorTextTopToCircleBorder;

    /**点击内圈圆的监听响应*/
    private InnerCircleClickListener mInnerCircleClickListener;
    /**点击花瓣的监听*/
    private PetalClickListener mPetalClickListener;
    /**点击外围扇形的监听*/
    private SectorClickListener mSectorClickListener;
    /**花瓣信息（包含花瓣的列表信息）*/
    private PetalsInfo mPetalsInfo;
    /**是否触摸内圈*/
    private boolean mIsTouchInnerCircle;

    private double mLastDeltaAngle;
    private double mDeltaAngle;
    private float mTouchSlop;
    private float mTouchDownX;
    private float mTouchDownY;
    private float mTouchMoveX;
    private float mTouchMoveY;
    private boolean mIsValid;

    /**是否可以转动*/
    private boolean mCanTurned;
    /**是否经历过action_move*/
    private boolean mIsExperenceActionMove = false;

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
        mNormalFillSectorColor = typedArray.getColor(R.styleable.PetalsRoundView_normalFillSectorColor, getResources().getColor(R.color.light_green));
        mNormalFillSectorColorClick = typedArray.getColor(R.styleable.PetalsRoundView_normalFillSectorColorClick, getResources().getColor(R.color.light_green_pressed));
        mHighlightFillSectorColor = typedArray.getColor(R.styleable.PetalsRoundView_highlightFillSectorColor, getResources().getColor(R.color.light_yellow));
        mHighlightFillSectorColorClick = typedArray.getColor(R.styleable.PetalsRoundView_highlightFillSectorColorClick, getResources().getColor(R.color.light_yellow_pressed));
        mRatioOfPetalImgHeightToCircle = typedArray.getFloat(R.styleable.PetalsRoundView_ratioOfPetalImgHeightToCircle, 0.75f);
        mCircleBorderStrokeWidth = typedArray.getDimension(R.styleable.PetalsRoundView_circleBorderStrokeWidth, getResources().getDimension(R.dimen.petals_round_view_circle_border_stroke_width));
        mThinRadiusLineWidth = typedArray.getDimension(R.styleable.PetalsRoundView_thinRadiusLineWidth, getResources().getDimension(R.dimen.petals_round_view_thin_radius_line_width));
        mThickRadiusLineWidth = typedArray.getDimension(R.styleable.PetalsRoundView_thickRadiusLineWidth, getResources().getDimension(R.dimen.petals_round_view_thick_radius_line_width));
        mRadiusLineColor = typedArray.getColor(R.styleable.PetalsRoundView_radiusLineColor, getResources().getColor(R.color.middle_blue));
        mInnerCircleRadius = typedArray.getDimension(R.styleable.PetalsRoundView_innerCircleRadius, getResources().getDimension(R.dimen.petals_round_view_inner_circle_radius));
        mInnerRingWidth = typedArray.getDimension(R.styleable.PetalsRoundView_innerRingWidth, getResources().getDimension(R.dimen.petals_round_view_inner_ring_width));
        mInnerRingColor = typedArray.getColor(R.styleable.PetalsRoundView_innerRingColor, getResources().getColor(R.color.light_gray_ring));
        mInnerCircleExceptRingColor = typedArray.getColor(R.styleable.PetalsRoundView_innerCircleExceptRingColor, getResources().getColor(R.color.middle_black));
        mInnerCircleTextSize = typedArray.getDimension(R.styleable.PetalsRoundView_innerCircleTextSize, getResources().getDimension(R.dimen.petals_round_view_inner_circle_text_size));
        mInnerCircleTextColor = typedArray.getColor(R.styleable.PetalsRoundView_innerCircleTextColor, getResources().getColor(R.color.white));
        mOuterSectorTextSize = typedArray.getDimension(R.styleable.PetalsRoundView_outerSectorTextSize, getResources().getDimension(R.dimen.petals_round_view_outer_sector_text_size));
        mOuterSectorTextColor = typedArray.getColor(R.styleable.PetalsRoundView_outerSectorTextColor, getResources().getColor(R.color.sector_text_color));
        mOuterSectorHighlightTextColor = typedArray.getColor(R.styleable.PetalsRoundView_outerSectorHighlightTextColor, getResources().getColor(R.color.black));
        mInnerCircleMaxLenEachLine = typedArray.getInt(R.styleable.PetalsRoundView_innerCircleMaxLenEachLine, INNER_CIRCLE_MAX_LEN_EACH_LINE);
        mPetalTextSize = typedArray.getDimension(R.styleable.PetalsRoundView_petalTextSize, getResources().getDimension(R.dimen.petals_round_view_petal_text_size));
        mPetalNormalTextColor = typedArray.getColor(R.styleable.PetalsRoundView_petalNormalTextColor, getResources().getColor(R.color.white));
        mPetalHighlightTextColor = typedArray.getColor(R.styleable.PetalsRoundView_petalHighlightTextColor, getResources().getColor(R.color.black));
        mPetalAreaMaxLenEachLine = typedArray.getInt(R.styleable.PetalsRoundView_petalAreaMaxLenEachLine, PETAL_AREA_MAX_LEN_EACH_LINE);
        mPetalAreaTextMaxLines = typedArray.getInt(R.styleable.PetalsRoundView_petalAreaTextMaxLines, PETAL_AREA_TEXT_MAX_LINES);
        mPetalAreaBottomTextSize = typedArray.getDimension(R.styleable.PetalsRoundView_petalAreaBottomTextSize, getResources().getDimension(R.dimen.petals_round_view_petal_bottom_text_size));
        mPetalAreaBottomTextColor = typedArray.getColor(R.styleable.PetalsRoundView_petalAreaBottomTextColor, getResources().getColor(R.color.gray));
        mDistanceOfPetalBottomTextToInnerCircleCorner = typedArray.getFloat(R.styleable.PetalsRoundView_distanceOfPetalBottomTextToInnerCircleCorner, DISTANCE_RAIDO_OF_PETAL_BOTTOM_TEXT_TO_INNER_CIRCLE_CORNER);
        mRatioOfSectorTextTopToCircleBorder = typedArray.getFloat(R.styleable.PetalsRoundView_ratioOfSectorTextTopToCircleBorder, RATIO_OF_SECTOR_TEXT_TOP_TO_CIRCEL_BORDER);
        mCanTurned = typedArray.getBoolean(R.styleable.PetalsRoundView_canTurnInCircle, false);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public void setHighlightIndex(int mHighlightGroupIndex,int mHighlightChildIndex) {
        if (!(this.mHighlightGroupIndex == mHighlightGroupIndex && mHighlightChildIndexList != null && mHighlightChildIndexList.size() == 1 && mHighlightChildIndexList.get(0) != null && mHighlightChildIndexList.get(0) == mHighlightChildIndex)) {
            this.mHighlightGroupIndex = mHighlightGroupIndex;
            dealWithHighlightChildIndexList();
            mHighlightChildIndexList.add(mHighlightChildIndex);
            invalidate();
        }
    }

    public void setHighlightIndex(int mHighlightGroupIndex,String highlightChildName) {
        convertHighlightNameToIndex(mHighlightGroupIndex, highlightChildName);
        invalidate();
    }

    private void dealWithHighlightChildIndexList() {
        if (mHighlightChildIndexList == null) {
            mHighlightChildIndexList = new ArrayList<>();
        } else {
            mHighlightChildIndexList.clear();
        }
    }

    public void setHighlightChildIndexList(List<Integer> mHighlightChildIndexList) {
        this.mHighlightChildIndexList = mHighlightChildIndexList;
    }

    public void setHighlightName(String highlightGroupName, String highlightChildName) {
        convertHighlightNameToIndex(highlightGroupName, highlightChildName);
        invalidate();
    }

    /**
     * 将将高亮的名字转化为对应的序号
     * @param highlightGroupIndex
     * @param highlightChildIndexs
     */
    private void convertHighlightNameToIndex(int highlightGroupIndex, String highlightChildIndexs) {
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null || mPetalsInfo.getPetalList().size() == 0) {
            return;
        }

        List<PetalsInfo.PetalEntity> list = mPetalsInfo.getPetalList();
        if (highlightGroupIndex >= 0 && highlightGroupIndex < list.size()) {
            mHighlightGroupIndex = highlightGroupIndex;
            dealWithHighlightChildIndexList();
            PetalsInfo.PetalEntity bean = list.get(highlightGroupIndex);
            if (bean != null) {
                List<String> childList = bean.getChildList();
                if (childList != null && childList.size() != 0) {
                    if (!Utils.isNullOrNil(highlightChildIndexs)) {
                        String[] arr = highlightChildIndexs.split(Constants.CHARACTER_COMMA);
                        if (arr != null) {
                            for (int i = 0; i < arr.length; i++) {
                                String str = arr[i];
                                int index = -1;
                                try {
                                    index = Integer.parseInt(str);
                                    mHighlightChildIndexList.add(index);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } else {
                        mHighlightChildIndexList.add(HIGHLIGHT_CHILD_INDEX_NO_VALUE);
                    }
                }
            }
        }
    }

    /**
     * 将将高亮的名字转化为对应的序号
     * @param highlightGroupName
     * @param highlightChildName
     */
    private void convertHighlightNameToIndex(String highlightGroupName, String highlightChildName) {
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null || mPetalsInfo.getPetalList().size() == 0) {
            return;
        }
        mHighlightGroupIndex = -1;
        if (TextUtils.isEmpty(highlightGroupName)) {
            mHighlightGroupIndex = -1;
            return;
        }
        List<PetalsInfo.PetalEntity> list = mPetalsInfo.getPetalList();
        dealWithHighlightChildIndexList();
        loop:
        for(int i = 0; i < list.size(); i++) {
            PetalsInfo.PetalEntity bean = list.get(i);
            if (bean != null) {
                if (Utils.getNullOrNil(bean.getName()).equals(highlightGroupName)) {
                    mHighlightGroupIndex = i;
                    List<String> childList = bean.getChildList();
                    if (childList != null && childList.size() != 0) {
                        if (!Utils.isNullOrNil(highlightChildName)) {
                            String[] arr = highlightChildName.split(Constants.CHARACTER_COMMA);
                            if (arr != null) {
                                for(int j = 0; j < arr.length; j++) {
                                    String str = arr[j];
                                    if (!Utils.isNullOrNil(str)) {
                                        for(int k = 0; k < childList.size(); k++) {
                                            String childBean = childList.get(k);
                                            if (childBean != null) {
                                                if (Utils.getNullOrNil(childBean).equals(str)) {
                                                    mHighlightChildIndexList.add(k);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            mHighlightChildIndexList.add(HIGHLIGHT_CHILD_INDEX_NO_VALUE);
                        }
                    }
                    break;
                }
            }
        }
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

        if (mCanTurned) {
            canvas.save();
            canvas.rotate((float)mDeltaAngle, mCircleRadius, mCircleRadius);
        }
        drawCircleAndSectorPart(canvas);
        drawLinePartAndSectorText(canvas);
        drawPetalsPart(canvas);
        drawPetalsText(canvas);
        drawInnerCircle(canvas);
        if (mCanTurned) {
            canvas.restore();
        }
    }

    private boolean isHighlightChildIndexListOnlyContainNoValue() {
        boolean flag = (mHighlightChildIndexList != null && mHighlightChildIndexList.size() == 1 && mHighlightChildIndexList.get(0) != null && mHighlightChildIndexList.get(0) == HIGHLIGHT_CHILD_INDEX_NO_VALUE);
        return flag;
    }

    /**
     * 画圆形及扇形区域
     * @param canvas
     */
    private void drawCircleAndSectorPart(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        if (mPetalsInfo != null && mPetalsInfo.getPetalList() != null && mPetalsInfo.getPetalList().size() != 0) {
            mEachPetalAngle = 2 * Math.PI / mPetalsInfo.getPetalList().size();
            float left = mCircleBorderStrokeWidth;
            float right = 2 * mCircleRadius - mCircleBorderStrokeWidth;
            float top = mCircleBorderStrokeWidth;
            float bottom = 2 * mCircleRadius - mCircleBorderStrokeWidth;
            RectF rectF = new RectF(left, top, right, bottom);
            for(int i = 0; i < mPetalsInfo.getPetalList().size(); i++) {
                float startAngle = (float)Math.toDegrees(i * mEachPetalAngle - Math.PI / 2);
                int size = mPetalsInfo.getPetalList().get(i).getChildList().size();
                double mEachPetalChildAngle = mEachPetalAngle / size;
                float startChildAngle = startAngle;
                for (int j = 0; j < size; j++) {
                    float startDrawChildAngle = startChildAngle + (float)Math.toDegrees(mEachPetalChildAngle) * j;
                    if(i == mHighlightGroupIndex && mHighlightChildIndexList != null && !isHighlightChildIndexListOnlyContainNoValue()) {
                        if (mHighlightChildIndexList.size() != 0) {          // 如果变量highlightChildIndexList有值,只是高亮其中几个方法，并不是全部
                            if (mHighlightChildIndexList.contains(j)) {
                                if (i == mTouchDownGroupIndex && j == mTouchDownChildIndex) {
                                    mPaint.setColor(mHighlightFillSectorColorClick);
                                } else {
                                    mPaint.setColor(mHighlightFillSectorColor);
                                }
                            } else {
                                if(i == mTouchDownGroupIndex && j == mTouchDownChildIndex) {
                                    mPaint.setColor(mNormalFillSectorColorClick);
                                } else {
                                    mPaint.setColor(mNormalFillSectorColor);
                                }
                            }
                            canvas.drawArc(rectF, startDrawChildAngle, (float)Math.toDegrees(mEachPetalChildAngle), true, mPaint);
                        } else {
                            if (i == mTouchDownGroupIndex && j == mTouchDownChildIndex) {
                                mPaint.setColor(mHighlightFillSectorColorClick);
                            } else {
                                mPaint.setColor(mHighlightFillSectorColor);
                            }
                            canvas.drawArc(rectF, startDrawChildAngle, (float)Math.toDegrees(mEachPetalChildAngle), true, mPaint);
                        }
                    } else if(i == mHighlightGroupIndex && mHighlightChildIndexList != null && mHighlightChildIndexList.size() == 1 && mHighlightChildIndexList.get(0) != null && mHighlightChildIndexList.get(0) == HIGHLIGHT_CHILD_INDEX_NO_VALUE) {
                        if(i == mTouchDownGroupIndex && j == mTouchDownChildIndex) {
                            mPaint.setColor(mHighlightFillSectorColorClick);
                        } else {
                            mPaint.setColor(mHighlightFillSectorColor);
                        }
                        canvas.drawArc(rectF, startDrawChildAngle, (float)Math.toDegrees(mEachPetalChildAngle), true, mPaint);
                    } else {
                        if(i == mTouchDownGroupIndex && j == mTouchDownChildIndex) {
                            mPaint.setColor(mNormalFillSectorColorClick);
                        } else {
                            mPaint.setColor(mNormalFillSectorColor);
                        }
                        canvas.drawArc(rectF, startDrawChildAngle, (float)Math.toDegrees(mEachPetalChildAngle), true, mPaint);
                    }

                }
            }
        } else {
            mPaint.setColor(mNormalFillSectorColor);
            canvas.drawCircle(mCircleRadius, mCircleRadius, mCircleRadius - mCircleBorderStrokeWidth, mPaint);
        }
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
        float delta1 = (float)(mRatioOfSectorTextTopToCircleBorder * (mCircleRadius - mCircleBorderStrokeWidth));
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
                        mTextPaint.setLetterSpacing(0.1f);
                        if (i == mHighlightGroupIndex && !isHighlightChildIndexListOnlyContainNoValue()) {
                            if (mHighlightChildIndexList != null && mHighlightChildIndexList.size() != 0) {
                                if (mHighlightChildIndexList.contains(j)) {
                                    mTextPaint.setColor(mOuterSectorHighlightTextColor);
                                } else {
                                    mTextPaint.setColor(mOuterSectorTextColor);
                                }
                            } else {
                                mTextPaint.setColor(mOuterSectorHighlightTextColor);
                            }
                        } else if (i == mHighlightGroupIndex && isHighlightChildIndexListOnlyContainNoValue()) {
                            mTextPaint.setColor(mOuterSectorHighlightTextColor);
                        } else {
                            mTextPaint.setColor(mOuterSectorTextColor);
                        }

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
                            float deltaHeight = delta1 / 2 + (k + 1 / 2) * subHeight;    // 每行文字中心离外层的圆边界高度差（不包含圆形外边边界stroke）
                            double radio = subWidth / ((mCircleRadius - mCircleBorderStrokeWidth - deltaHeight) * 2 * Math.PI / (petalsSize * childSize));
                            float deltaAngle = 0;                    // 每行文字与相应扇形缩进的角度
                            if (radio < 1 && radio > 0) {
                                deltaAngle = (float)((1 - radio) / 2 * Math.toDegrees(eachChildEachPetalAngle));
                            }
                            Path path = new Path();
                            float startAngle = (float)(Math.toDegrees(childAngle - eachChildEachPetalAngle) - Math.toDegrees(Math.PI / 2)) + deltaAngle;
                            float sweepAngle = (float)Math.toDegrees(eachChildEachPetalAngle);
                            path.addArc(oval, startAngle, sweepAngle);
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
        Log.w(TAG, "drawPetalsPart: mHighLightGroupIndex = " + mHighlightGroupIndex);
        if (mHighlightGroupIndex >= 0 && mHighlightFillSectorColorClick < mPetalsInfo.getPetalList().size()) {     // 如果有高亮序号的话
            int firstDrawIndex = mHighlightGroupIndex <= mPetalsInfo.getPetalList().size() - 1 ? mHighlightGroupIndex + 1 : 0;
            int firstDrawIndex1 = firstDrawIndex % mPetalsInfo.getPetalList().size();
            drawPetalsPart(canvas, true, true, firstDrawIndex1);
            drawPetalsPart(canvas, false, false, mHighlightGroupIndex);
            drawPetalsPart(canvas, true, false, mHighlightGroupIndex);
            for(int i = mHighlightGroupIndex - 1 + mPetalsInfo.getPetalList().size(); i > firstDrawIndex; i--) {
                int index = i % mPetalsInfo.getPetalList().size();
                drawPetalsPart(canvas, false, true, index);
                drawPetalsPart(canvas, true, true, index);
            }
            drawPetalsPart(canvas, false, true, firstDrawIndex1);
        } else {
            int firstDrawIndex = 0;
            drawPetalsPart(canvas, true, true, firstDrawIndex);
            for(int i = mPetalsInfo.getPetalList().size() - 1; i > firstDrawIndex; i--) {
                drawPetalsPart(canvas, false, true, i);
                drawPetalsPart(canvas, true, true, i);
            }
            drawPetalsPart(canvas, false, true, firstDrawIndex);
        }
    }

    private void drawPetalsPart(Canvas canvas, boolean isLeftPetalPart, boolean isNormalPetal, int index) {
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null || mPetalsInfo.getPetalList().size() == 0) {
            return;
        }
        Log.w(TAG, "drawPetalsPart: index = "+ index + "; isLeftPetalPart = " + isLeftPetalPart + "; isNormalPetal = " + isNormalPetal + "; mTouchDownGroupIndex = " +mTouchDownGroupIndex + "; mTouchDownChildIndex = " + mTouchDownChildIndex);
        int resId = R.drawable.ic_normal_petal_left_even;
        if (isLeftPetalPart && isNormalPetal) {
            //如果正处于点击下的状态
            if(index == mTouchDownGroupIndex && mTouchDownChildIndex == -1) {
                resId = R.drawable.ic_highlight_petal_left;
            } else {
                if (index %2 == 0) {
                    resId = R.drawable.ic_normal_petal_left_even;
                } else {
                    resId = R.drawable.ic_normal_petal_left_odd;
                }
            }
        } else if (!isLeftPetalPart && isNormalPetal) {
            //如果正处于点击下的状态
            if(index == mTouchDownGroupIndex && mTouchDownChildIndex == -1) {
                resId = R.drawable.ic_highlight_petal_right;
            } else {
                if (index % 2 == 0) {
                    resId = R.drawable.ic_normal_petal_right_even;
                } else {
                    resId = R.drawable.ic_normal_petal_right_odd;
                }
            }
        } else if (isLeftPetalPart && !isNormalPetal) {
            //如果需要高亮的花瓣index为-1，则不点亮任何花瓣
            if(index == -1) {
                if (index % 2 == 0) {
                    resId = R.drawable.ic_normal_petal_left_even;
                } else {
                    resId = R.drawable.ic_normal_petal_left_odd;
                }
            } else {
                //如果正处于点击下的状态
                if(index == mTouchDownGroupIndex && mTouchDownChildIndex == -1) {
                    resId = R.drawable.ic_highlight_petal_left_pressed;
                } else {
                    resId = R.drawable.ic_highlight_petal_left;
                }
            }
        } else if (!isLeftPetalPart && !isNormalPetal) {
            //如果需要高亮的花瓣index为-1，则不点亮任何花瓣
            if(index == -1) {
                if (index % 2 == 0) {
                    resId = R.drawable.ic_normal_petal_right_even;
                } else {
                    resId = R.drawable.ic_normal_petal_right_odd;
                }
            } else {
                //如果正处于点击下的状态
                if(index == mTouchDownGroupIndex && mTouchDownChildIndex == -1) {
                    resId = R.drawable.ic_highlight_petal_right_pressed;
                } else {
                    resId = R.drawable.ic_highlight_petal_right;
                }
            }
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float raido = (int)(mCircleRadius * mRatioOfPetalImgHeightToCircle) / (float)height;
        int newWidth = (int)(width * raido);
        int newHeight = (int)(height * raido);
        Bitmap targetBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, false);
        if (targetBitmap != null) {
            bitmap.recycle();

            canvas.save();
            float translateX = mCircleRadius - newWidth;
            float rotateX = newWidth;
            if (!isLeftPetalPart) {
                translateX = mCircleRadius;
                rotateX = 0;
            }
            canvas.translate(translateX,  mCircleRadius - newHeight);
            float degree = (float)Math.toDegrees((index + 0.5) * mEachPetalAngle);
            //旋转的角度是以度为单位
            canvas.rotate(degree, rotateX, newHeight);

            canvas.drawBitmap(targetBitmap, 0, 0, null);
            canvas.restore();
        }
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
            if (entity == null) {
                continue;
            }
            String name = entity.getName();
//            name = "伟荣放的地方劳动力佛东大路发电量劳动法";
            double sharpCornerAngle = (i + 1f / 2) * mEachPetalAngle;
            int lines = name.length() % mPetalAreaMaxLenEachLine == 0 ? name.length() / mPetalAreaMaxLenEachLine : name.length() / mPetalAreaMaxLenEachLine + 1;
            double centerPointX = mCircleRadius + mDistanceOfRectCenterPointToCircleCenter * Math.sin(sharpCornerAngle);
            double centerPointY = mCircleRadius - mDistanceOfRectCenterPointToCircleCenter * Math.cos(sharpCornerAngle);
            mTextPaint.setTextSize(mPetalTextSize);
            mTextPaint.setLetterSpacing(0f);
            if (i == mHighlightGroupIndex) {
                mTextPaint.setColor(mPetalHighlightTextColor);
            } else {
                mTextPaint.setColor(mPetalNormalTextColor);
            }
            float fontHeight = mTextPaint.getFontMetrics().bottom - mTextPaint.getFontMetrics().top;
            for(int j = 0; j < lines; j++) {
                if (j > (mPetalAreaTextMaxLines - 1)) {        //加一个限制:最多显示mPetalAreaTextMaxLines行
                    break;
                }
                String childName = "";
                int linesTemp = Math.min(lines, mPetalAreaTextMaxLines);
                if (j == linesTemp - 1) {
                    childName = name.substring(mPetalAreaMaxLenEachLine * j, name.length());
                    if (lines > mPetalAreaTextMaxLines) {               //如果行数超过最大限制行数，则在最大限制行数末尾最后一个字符替换成字符"..."
                        childName = name.substring(mPetalAreaMaxLenEachLine * j, mPetalAreaMaxLenEachLine *  (j + 1) - 1)+ "...";
                    }
                } else {
                    childName = name.substring(mPetalAreaMaxLenEachLine * j, mPetalAreaMaxLenEachLine * (j + 1));
                }
                float width = mTextPaint.measureText(childName);
                canvas.drawText(childName, (float)(centerPointX - width / 2), (float)(centerPointY + (j + 1 - lines / 2f) * fontHeight - mTextPaint.getFontMetrics().bottom), mTextPaint);
                Log.v(TAG, "bottom = " + mTextPaint.getFontMetrics().bottom + "; des = " + mTextPaint.getFontMetrics().descent + "; asc = " + mTextPaint.getFontMetrics().ascent + "; leading = " + mTextPaint.getFontMetrics().leading + "; top = " + mTextPaint.getFontMetrics().top + "; fontSpace = " + mTextPaint.getFontSpacing());
            }

            //画花瓣底部的文本，也是需要使用每片花瓣尖角对应的角度, 一行的方式显示，而且呈圆形的方式显示
            String catalogName = entity.getName();
            if (!TextUtils.isEmpty(catalogName)) {
                if (catalogName.contains(Utils.DASH)) {             // 如果有字符"-"，则取"-"之前的字符
                    catalogName = catalogName.split(Utils.DASH)[0];
                }
                if (!TextUtils.isEmpty(catalogName)) {
                    mTextPaint.setTextSize(mPetalAreaBottomTextSize);
                    mTextPaint.setColor(mPetalAreaBottomTextColor);
                    float width = mTextPaint.measureText(catalogName);
                    //文字显示的圆半径
                    double radius = mInnerCircleRadius + mDistanceOfPetalBottomTextToInnerCircleCorner * mCircleRadius;
                    //文字占圆弧的比例
                    double radio = width / (Math.PI * 2 * radius / list.size());
                    float deltaAngle = 0;                    // 每行文字与相应扇形缩进的角度
                    if (radio < 1 && radio > 0) {
                        deltaAngle = (float)((1 - radio) / 2 * Math.toDegrees(mEachPetalAngle));
                    }

                    RectF oval = new RectF((float)(mCircleRadius - radius), (float)(mCircleRadius - radius),  (float)(mCircleRadius + radius), (float)(mCircleRadius + radius));
                    Path path = new Path();
                    double startAngle = (float)(Math.toDegrees(sharpCornerAngle) - Math.toDegrees(Math.PI / 2)) + deltaAngle / 2;  // 居中偏左一点
                    float sweepAngle = (float)Math.toDegrees(mEachPetalAngle);
                    path.addArc(oval, (float)startAngle, sweepAngle);
                    canvas.drawTextOnPath(catalogName, path, 0, 0, mTextPaint);
                }
            }
        }
    }

    /**
     * 画内部圆
     * @param canvas
     */
    private void drawInnerCircle(Canvas canvas) {
//        mPaint.setColor(mInnerRingColor);
//        mPaint.setStyle(Paint.Style.STROKE);
//        mPaint.setStrokeWidth(mInnerRingWidth);
//        canvas.drawCircle(mCircleRadius, mCircleRadius, mInnerCircleRadius - mInnerRingWidth, mPaint);
//
//        mPaint.setColor(mInnerCircleExceptRingColor);
//        mPaint.setStyle(Paint.Style.FILL);
//        canvas.drawCircle(mCircleRadius, mCircleRadius, mInnerCircleRadius - mInnerRingWidth, mPaint);

        int resId = R.drawable.ic_inner_circle;
        if (mIsTouchInnerCircle) {
            resId = R.drawable.ic_inner_circle;
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float raido = (float)(2 * mInnerCircleRadius) / height;
        float newWidth = width * raido;
        float newHeight = height * raido;
        Bitmap targetBitmap = Bitmap.createScaledBitmap(bitmap, (int)newWidth, (int)newHeight, false);
        if (targetBitmap != null) {
            bitmap.recycle();
            canvas.drawBitmap(targetBitmap, mCircleRadius - mInnerCircleRadius, mCircleRadius - mInnerCircleRadius, null);
        }

        if (mPetalsInfo != null) {
            String name = Utils.getNullOrNil(mPetalsInfo.getName());
            if (mInnerCircleMaxLenEachLine <= 0) {
                mInnerCircleMaxLenEachLine = INNER_CIRCLE_MAX_LEN_EACH_LINE;
            }
            //是否整除
            boolean isDivisible = name.length() % mInnerCircleMaxLenEachLine == 0;
            int textline = isDivisible ? name.length() / mInnerCircleMaxLenEachLine : name.length() / mInnerCircleMaxLenEachLine + 1;   // 文字行数

            mTextPaint.setTextSize(mInnerCircleTextSize);
            mTextPaint.setColor(mInnerCircleTextColor);
            mTextPaint.setLetterSpacing(0f);
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

    public void setInnerCircleClickListener(InnerCircleClickListener mInnerCircleClickListener) {
        this.mInnerCircleClickListener = mInnerCircleClickListener;
    }

    public void setPetalClickListener(PetalClickListener mPetalClickListener) {
        this.mPetalClickListener = mPetalClickListener;
    }

    public void setSectorClickListener(SectorClickListener mSectorClickListener) {
        this.mSectorClickListener = mSectorClickListener;
    }

    /**
     * 手指按下时的控件
     */
    public int mTouchDownGroupIndex = -1;
    public int mTouchDownChildIndex = -1;
    /**
     * 手指move最后时所在的控件
     */
    public int mTouchUpGroupIndex = -1;
    public int mTouchUpChildIndex = -1;
    /**
     * 只有在手指按下时和手指最后move到的所在控件是
     * 同一个控件，则会触发点击事件
     */
    private boolean mIsClickSameArea = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPetalsInfo == null || mPetalsInfo.getPetalList() == null) {
            return false;
        }
        int action = event.getAction();
        float touchX = event.getX();
        float touchY = event.getY();
        if (action == MotionEvent.ACTION_DOWN) {
            mTouchDownX = touchX;
            mTouchDownY = touchY;
            mIsExperenceActionMove = false;
            if (Math.sqrt(Math.pow(touchX - mCircleRadius, 2) + Math.pow(touchY - mCircleRadius, 2)) >= mCircleRadius) {
                mTouchDownGroupIndex = -1;
                mTouchDownChildIndex = -1;
                mTouchUpGroupIndex = -1;
                mTouchUpChildIndex = -1;
                invalidate();
                return true;
            }
            if (mPetalsInfo.getPetalList().size() != 0) {
                for(int i = 0; i < mPetalsInfo.getPetalList().size(); i++) {
                    PetalsInfo.PetalEntity entity = mPetalsInfo.getPetalList().get(i);
                    if (entity != null && entity.getChildList() != null) {
                        for(int j = 0; j < entity.getChildList().size(); j++){
                            if (isBelongToCertainArea(touchX, touchY, i, j)) {
                                //mSectorClickListener.onClick(i, j);
                                this.mTouchDownGroupIndex = i;
                                this.mTouchDownChildIndex = j;
                                invalidate();
                                break;
                            }
                        }
                        String name = entity.getName();
                        if (TextUtils.isEmpty(name)) {
                            continue;
                        }
                        if (isBelongToCertainRect(touchX, touchY, i, name)) {
                            //mPetalClickListener.onClick(i);
                            this.mTouchDownGroupIndex = i;
                            this.mTouchDownChildIndex = -1;
                            invalidate();
                            break;
                        }
                    }
                }
            }
            if (judgePointIsInCircle(touchX, touchY, mInnerCircleRadius)) {
                mIsTouchInnerCircle = true;
                invalidate();
            }
            return true;
        } else if (action == MotionEvent.ACTION_MOVE) {
            mTouchMoveX = touchX;
            mTouchMoveY = touchY;
            mIsExperenceActionMove = true;
            double downAngle = getAngle(mTouchDownX, mTouchDownY);
            double moveAngle = getAngle(mTouchMoveX, mTouchMoveY);
            if (mCanTurned) {
                mDeltaAngle = moveAngle - downAngle + mLastDeltaAngle;
            }
            if (Math.sqrt(Math.pow(mTouchMoveX - mTouchDownX, 2) + Math.pow(mTouchMoveY - mTouchDownY, 2)) >= mTouchSlop && mCanTurned) {
                invalidate();
                mIsValid = false;
                return true;
            } else {
                mIsValid = true;
            }
            if (mPetalsInfo.getPetalList().size() != 0) {
                for(int i = 0; i < mPetalsInfo.getPetalList().size(); i++) {
                    PetalsInfo.PetalEntity entity = mPetalsInfo.getPetalList().get(i);
                    if (entity != null && entity.getChildList() != null) {
                        for(int j = 0; j < entity.getChildList().size(); j++){
                            if (isBelongToCertainArea(touchX, touchY, i, j)) {
                                //mSectorClickListener.onClick(i, j);
                                this.mTouchUpGroupIndex = i;
                                this.mTouchUpChildIndex = j;
                                invalidate();
                                break;
                            }
                        }
                        String name = entity.getName();
                        if (TextUtils.isEmpty(name)) {
                            continue;
                        }
                        if (isBelongToCertainRect(touchX, touchY, i, name)) {
                            //mPetalClickListener.onClick(i);
                            this.mTouchUpGroupIndex = i;
                            this.mTouchUpChildIndex = -1;
                            invalidate();
                            break;
                        }
                    }
                }
            }
            if(mTouchDownChildIndex == mTouchUpChildIndex && mTouchDownGroupIndex == mTouchUpGroupIndex) {
                mIsClickSameArea = true;
            } else {
                mIsClickSameArea = false;
            }
            return true;
        } else if (action == MotionEvent.ACTION_UP) {
            double downAngle = getAngle(mTouchDownX, mTouchDownY);
            double upAngle = getAngle(touchX, touchY);
            if (mCanTurned) {
                mLastDeltaAngle += upAngle - downAngle;
            }
            if (!mIsValid) {
                mTouchDownGroupIndex = -1;
                mTouchDownChildIndex = -1;
                mTouchUpGroupIndex = -1;
                mTouchUpChildIndex = -1;
                invalidate();
                return true;
            }
            if((mIsClickSameArea && mIsExperenceActionMove) || !mIsExperenceActionMove) {
                if(mTouchDownGroupIndex >= 0) {
                    if(mTouchDownChildIndex >= 0) {
                        playSoundEffect(SoundEffectConstants.CLICK);  // 加个音效
                        mHighlightGroupIndex = mTouchUpGroupIndex;
                        dealWithHighlightChildIndexList();
                        mHighlightChildIndexList.add(mTouchUpChildIndex);
                        if (mSectorClickListener != null) {
                            mSectorClickListener.onClick(mTouchDownGroupIndex, mTouchDownChildIndex, true);
                        }
                    } else {
                        playSoundEffect(SoundEffectConstants.CLICK);  // 加个音效
                        mHighlightGroupIndex = mTouchUpGroupIndex;
                        dealWithHighlightChildIndexList();
                        mHighlightChildIndexList.add(HIGHLIGHT_CHILD_INDEX_NO_VALUE);
                        if (mPetalClickListener != null) {
                            mPetalClickListener.onClick(mTouchDownGroupIndex, true);
                            Log.w(TAG, "onTouchEvent: mTouchDownGroupIndex="+ mTouchDownGroupIndex +"; mTouchDownChildIndex="+ mTouchDownChildIndex);
                        }
                    }
                }
            }
            if (mIsTouchInnerCircle && judgePointIsInCircle(touchX, touchY, mInnerCircleRadius)) {
                playSoundEffect(SoundEffectConstants.CLICK);  // 加个音效
                if (mInnerCircleClickListener != null) {
                    mInnerCircleClickListener.onClick();
                }
            }
            mTouchDownGroupIndex = -1;
            mTouchDownChildIndex = -1;
            mTouchUpGroupIndex = -1;
            mTouchUpChildIndex = -1;
            mIsTouchInnerCircle = false;
            invalidate();
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
        if (TextUtils.isEmpty(petalName)) {
            return false;
        }
        double sharpCornerAngle = (index + 1f / 2) * mEachPetalAngle;
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
        double radius = Math.sqrt(Math.pow(x - mCircleRadius, 2) + Math.pow(y - mCircleRadius, 2));
        float targetX = (float)(mCircleRadius + radius * Math.sin(Math.toRadians(getAngle(x, y) - mDeltaAngle)));
        float targetY = (float)(mCircleRadius - radius * Math.cos(Math.toRadians(getAngle(x, y) - mDeltaAngle)));
        Log.v(TAG, "isBelongToCertainRect, targetX = " + targetX + "; targetY = " + targetY + "; index = " + index + "; sharpCornerAngle = " + Math.toDegrees(sharpCornerAngle) + "; result = " + rect.contains(targetX, targetY) + "; mDeltaAngle = " + mDeltaAngle + "; mCircle = " + mCircleRadius + "; getAngle(x, y) = " + getAngle(x, y));
        return rect.contains(targetX, targetY);
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

        boolean result = judgePointIsInCircle(x, y, mCircleRadius - mCircleBorderStrokeWidth);
        result = result && !judgePointIsInCircle(x, y, mCircleRadius * mRatioOfPetalImgHeightToCircle);
        double angle = (getAngle(x, y) - mDeltaAngle  + Math.toDegrees(2 * Math.PI)) % Math.toDegrees(2 * Math.PI);
        double startAngle = Math.toDegrees(groupIndex * mEachPetalAngle + childIndex * mEachPetalAngle / entity.getChildList().size());
        double endAngle = Math.toDegrees(groupIndex * mEachPetalAngle + (childIndex + 1) * mEachPetalAngle / entity.getChildList().size());
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
     * 点击内圈的响应
     */
    public interface InnerCircleClickListener {
        public void onClick();
    }

    /**
     * 点击花瓣的监听
     */
    public interface PetalClickListener{
        public void onClick(int index, boolean isFromUser);
    }

    /**
     * 点击外围扇形的点击监听
     */
    public interface SectorClickListener {
        public void onClick(int groupIndex, int childIndex, boolean isFromUser);
    }

    /**
     * 根据groupIndex实现点击花瓣的效果
     * @param groupIndex
     */
    public void performPetalClick(int groupIndex, boolean isFromUser) {
        if (mPetalClickListener != null) {
            mPetalClickListener.onClick(groupIndex, isFromUser);
        }
    }

    /**
     * 根据groupIndex、childIndex实现点击扇形的效果
     * @param groupIndex
     */
    public void performSectorClick(int groupIndex, int childIndex, boolean isFromUser) {
        if (mSectorClickListener != null) {
            mSectorClickListener.onClick(groupIndex, childIndex, isFromUser);
        }
    }

    /**
     * 根据highlightGroupName实现点击花瓣的效果
     * @param highlightGroupName
     */
    public void performPetalClick(String highlightGroupName, boolean isFromUser) {
        convertHighlightNameToIndex(highlightGroupName, "");
        if (mPetalClickListener != null) {
            mPetalClickListener.onClick(mHighlightGroupIndex, isFromUser);
        }
    }
}