package com.ztq.sdk.widget;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.ztq.sdk.R;
import com.ztq.sdk.utils.Utils;

/**
 * Created by ztq on 2019/11/6.
 */
public class CardView extends View {
    private final String TAG = "noahedu.CardView";
    private Bitmap[] mCards = new Bitmap[7];
    private int[] mImgId = new int[]{R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3, R.drawable.pic_4, R.drawable.pic_5, R.drawable.pic_6, R.drawable.pic_7};
    private Rect mScreenRect;
    private int mCount = 0;
    private Rect rect = new Rect();

    public CardView(Context context) {
        super(context);
        init();
    }

    public CardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        for (int i = 0; i < mCards.length; i++) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), mImgId[i]);
            mCards[i] = Bitmap.createScaledBitmap(bm, 510, 510, false);
        }
        setBackgroundColor(Color.WHITE);
        mScreenRect = new Rect(0, 0, Utils.getScreeenSize(getContext())[0], Utils.getScreeenSize(getContext())[1]);

        // 以下是属性动画的几句代码
        ValueAnimator va = ValueAnimator.ofObject(new TypeEvaluator() {
            @Override
            public Object evaluate(float fraction, Object startValue, Object endValue) {
                int start = (int) startValue;
                int end = (int) endValue;
                Log.v(TAG, "startValue = " + startValue + "; endValue = " + endValue + "; fraction = " + fraction);
                return 2 * (start + fraction * (end - start));
            }
        }, 0, 100);
        va.setInterpolator(new LinearInterpolator());
        va.setDuration(1666);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                Log.v(TAG, "value = " + value);
            }
        });
        va.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(50, 200);
        for (Bitmap bitmap : mCards) {
//            canvas.translate(150, 0);
//            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.translate(150, 0);

            mCount++;

            rect.left = 50 + mCount * 150;
            rect.right = 200 + mCount * 150;
            rect.top = 200;
            rect.bottom = 200 + bitmap.getHeight();
            if (rect.intersect(mScreenRect)) {
                Log.v(TAG, "mCount = " + mCount);
                canvas.save();
                canvas.clipRect(0, 0, 150, bitmap.getHeight());
                canvas.drawBitmap(bitmap, 0, 0, null);
                canvas.restore();
            }
        }
        canvas.restore();
    }
}