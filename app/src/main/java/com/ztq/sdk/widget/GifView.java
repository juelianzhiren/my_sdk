package com.ztq.sdk.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.ztq.sdk.R;

/**
 * Created by ztq on 2019/9/24.
 * 显示gif的控件，继承ImageView，首先需要设置activity硬件加速禁止，其次，这个显示效果不如框架好
 */
public class GifView extends ImageView {
    private final String TAG = "noahedu.GifView";
    private boolean mIsGifImage;
    private int mImageId;
    private Movie mMovie;
    private long mMovieStart;

    public GifView(Context context) {
        this(context, null);
    }

    public GifView(Context context,  AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性isgifimage
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GifView);
        mIsGifImage = array.getBoolean(R.styleable.GifView_isgifimage, true);
        array.recycle();//获取自定义属性完毕后需要recycle，不然会对下次获取造成影响

        //获取ImageView的默认src属性
        mImageId = attrs.getAttributeResourceValue( "http://schemas.android.com/apk/res/android", "src", 0);
        mMovie = Movie.decodeStream(getResources().openRawResource(mImageId));
    }

    public void setmIsGifImage(boolean mIsGifImage) {
        this.mIsGifImage = mIsGifImage;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if(mIsGifImage){                        //若为gif文件，执行DrawGifImage()，默认执行
            drawGifImage(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

    private void drawGifImage(Canvas canvas) {
        //获取系统当前时间
        long nowTime = android.os.SystemClock.currentThreadTimeMillis();
        if(mMovieStart == 0){
            //若为第一次加载，开始时间置为nowTime
            mMovieStart = nowTime;
        }
        if(mMovie != null){//容错处理
            int duration = mMovie.duration();//获取gif持续时间
            //如果gif持续时间小于100，可认为非gif资源，跳出处理
            if(duration > 100){
                //获取gif当前帧的显示所在时间点
                int realTime = (int) ((nowTime - mMovieStart) % duration);
                mMovie.setTime(realTime);
                Log.v(TAG, "duration = " + duration + "; realTime = " + realTime + "; width = " + mMovie.width() + "; height = " + mMovie.height() + "; " + getWidth() + "; " + getHeight() + "; left = " + getLeft() + "; " + getTop());
                //渲染gif图像
                mMovie.draw(canvas, 0, 0);
                invalidate();
            }
        }
    }
}