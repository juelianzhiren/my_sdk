package com.ztq.sdk.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ztq.sdk.R;
import com.ztq.sdk.constant.Constants;
import com.ztq.sdk.entity.SingleEntity;

/**
 * 单个汉字和拼音的view
 */
public class SingleTextWithPinyinView extends LinearLayout {
    private final String TAG = "noahedu.SingleTextWithPinyinView";
    private TextView textTv;
    private TextView pinyinTv;
    private Context context;
    private SingleEntity singleEntity;

    public SingleTextWithPinyinView(Context context) {
        this(context, null);
    }

    public SingleTextWithPinyinView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleTextWithPinyinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public void init() {
        View.inflate(context, R.layout.view_single_text_with_pinyin, this);
        textTv = findViewById(R.id.view_text_tv);
        textTv.setFontFeatureSettings("'liga' off");
        pinyinTv = findViewById(R.id.view_pinyin_tv);
        pinyinTv.setTypeface(Constants.mCarcassTypeface);
    }

    public void setData(SingleEntity singleEntity) {
        this.singleEntity = singleEntity;
        updateUI();
    }

    private void updateUI() {
        if (singleEntity == null) {
            return;
        }
        if ((singleEntity.getTextStyle() & SingleEntity.TEXT_STYLE_BOLD) != 0) {
            textTv.getPaint().setFakeBoldText(true);
            pinyinTv.getPaint().setFakeBoldText(true);
        } else {
            textTv.getPaint().setFakeBoldText(false);
            pinyinTv.getPaint().setFakeBoldText(false);
        }
        if ((singleEntity.getTextStyle() & SingleEntity.TEXT_STYLE_ITALIC) != 0) {
            textTv.getPaint().setTextSkewX(-0.2f);
            pinyinTv.getPaint().setTextSkewX(-0.2f);
        } else {
            textTv.getPaint().setTextSkewX(0);
            pinyinTv.getPaint().setTextSkewX(0);
        }
        textTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, singleEntity.getTextSize());
        textTv.setTextColor(singleEntity.getTextColor());
        textTv.setText(singleEntity.getStr());
        float textWidth = textTv.getPaint().measureText(singleEntity.getStr());

        pinyinTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, singleEntity.getPinyinSize());
        pinyinTv.setTextColor(singleEntity.getPinyinColor());
        pinyinTv.setText(singleEntity.getPinyin());
        float pinyinWidth = pinyinTv.getPaint().measureText(singleEntity.getPinyin());

        Log.v(TAG, "str = " + singleEntity.getStr() + ", textWidth = " + textWidth + ", pinyinWidth = " + pinyinWidth);
        if (pinyinWidth > textWidth) {
            Log.v(TAG, "while str = " + singleEntity.getStr() + ", textWidth = " + textWidth + ", pinyinWidth = " + pinyinWidth);
            pinyinTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, pinyinTv.getTextSize() * textWidth / pinyinWidth);
        }
    }
}