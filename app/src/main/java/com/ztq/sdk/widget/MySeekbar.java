package com.ztq.sdk.widget;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ztq.sdk.R;
import com.ztq.sdk.log.Log;

import java.util.List;

public class MySeekbar extends RelativeLayout {
    private static final String TAG = "noahedu.MySeekbar";
    private Context mContext;
    private List<String> mWordList;
    private RelativeLayout mCircleRL;
    private SeekBar mSeekBar;
    private RelativeLayout mWordRL;
    private int mCircleRadius;
    private int mTextSize;
    private TextView mLineTv;
    private SelectListener mSelectListener;

    public MySeekbar(Context context) {
        this(context, null);
    }

    public MySeekbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySeekbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.my_seekbar, this, true);
        mCircleRL = findViewById(R.id.my_seekbar_circle_rl);
        mSeekBar = findViewById(R.id.seekbar);
        mWordRL = findViewById(R.id.my_seekbar_word_rl);
        mLineTv = findViewById(R.id.seekbar_line_tv);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int index = progress - mSeekBar.getMin();
                if (mSelectListener != null) {
                    mSelectListener.select(index);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setSelectListener(SelectListener mSelectListener) {
        this.mSelectListener = mSelectListener;
    }

    public void setWordList(List<String> mWordList) {
        this.mWordList = mWordList;
        mSeekBar.setMin(1);
        mSeekBar.setMax(mWordList.size());
        requestFocus();
    }

    public void setCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (mWordList == null) {
            return;
        }
        for (int i = 0; i < mWordList.size(); i++) {
            ImageView iv = new ImageView(mContext);
            iv.setImageResource(R.drawable.shape_gray_circle);
            RelativeLayout.LayoutParams circleLp = new RelativeLayout.LayoutParams(2 * mCircleRadius, 2 * mCircleRadius);

            TextView tv = new TextView(mContext);
            String word = mWordList.get(i);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
            tv.setText(word);

            TextPaint textPaint = tv.getPaint();
            float textWidth = textPaint.measureText(word);
            float leftMargin = i * (width / (mWordList.size() - 1)) - textWidth / 2;
            circleLp.leftMargin = i * (width / (mWordList.size() - 1)) - mCircleRadius;
            if (i == 0) {
                leftMargin = 0;
                circleLp.leftMargin = 0;
            } else if (i == mWordList.size() - 1) {
                leftMargin = i * (width / (mWordList.size() - 1)) - textWidth;
                circleLp.leftMargin = i * (width / (mWordList.size() - 1)) - mCircleRadius * 2;
            }
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.leftMargin = (int)leftMargin;

            Log.v(TAG, "lp.leftMargin = " + circleLp.leftMargin);
            mCircleRL.addView(iv, circleLp);
            mWordRL.addView(tv, lp);
        }
        RelativeLayout.LayoutParams seekbarParam = (LayoutParams) mSeekBar.getLayoutParams();
        seekbarParam.width = width;
        mSeekBar.setLayoutParams(seekbarParam);

        RelativeLayout.LayoutParams lineParam = (LayoutParams) mLineTv.getLayoutParams();
        lineParam.width = width;
        mLineTv.setLayoutParams(lineParam);
    }

    public interface SelectListener {
        public void select(int index);
    }
}