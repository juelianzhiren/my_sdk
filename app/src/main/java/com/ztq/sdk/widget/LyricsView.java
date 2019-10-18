package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;

import com.ztq.sdk.R;
import com.ztq.sdk.entity.LyricsEntity;

import java.util.List;

/**
 * Created by ztq on 2019/10/17.
 */
public class LyricsView extends View {
    private final String TAG = "noahedu.LyricsView";
    private Context mContext;
    private List<LyricsEntity> mLyricsList;
    private float mDividerHeight;          // 歌词之间的垂直间距
    private GestureDetector mGestureDetector;

    public LyricsView(Context context) {
        super(context);
        init(context, null);
    }

    public LyricsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LyricsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LyricsView);
        mDividerHeight = typedArray.getDimension(R.styleable.LyricsView_lrcDividerHeight, context.getResources().getDimension(R.dimen.lrc_divider_height));
        Log.v(TAG, "mDividerHeight = " + mDividerHeight);
        typedArray.recycle();

        mGestureDetector = new GestureDetector(mContext, null);
    }

    /**
     * 获取歌词距离视图顶部的距离
     * 采用懒加载方式
     */
    private float getOffset(int line) {
        if (mLyricsList.get(line).getOffset() == Float.MIN_VALUE) {
            float offset = getHeight() / 2;
            for (int i = 1; i <= line; i++) {
                offset -= (mLyricsList.get(i - 1).getHeight() + mLyricsList.get(i).getHeight()) / 2 + mDividerHeight;
            }
            Log.v(TAG, "line = " + line + "; offset = " + offset);
            mLyricsList.get(line).setOffset(offset);
        }
        return mLyricsList.get(line).getOffset();
    }

    public void setLyriceList(List<LyricsEntity> lyricsList){
        mLyricsList = lyricsList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}