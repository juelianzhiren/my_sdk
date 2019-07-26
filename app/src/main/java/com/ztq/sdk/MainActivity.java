package com.ztq.sdk;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ztq.sdk.model.MyAnimationDrawable;
import com.ztq.sdk.utils.Utils;
import com.ztq.sdk.widget.MyImageView;
import com.ztq.sdk.widget.PinyinTextView;

public class MainActivity extends Activity {
    private final String TAG = "noahedu.MainActivity";
    private Context mContext;
    private MyImageView mIv;
    private Button mBtn;
    private MyAnimationDrawable myAnimationDrawable;
    private PinyinTextView mPinyinTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mIv = findViewById(R.id.iv);
        mBtn = findViewById(R.id.btn);

        myAnimationDrawable = new MyAnimationDrawable();

//        mIv.setImageResource(R.drawable.ic_candy_flash_02);
//        Bitmap bitmap = ((BitmapDrawable)mIv.getDrawable()).getBitmap();
//        int size = bitmap.getRowBytes() * bitmap.getHeight() / 1024 / 1024;
//        Log.v(TAG, "size = " + size + "m");
//
//        mIv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mIv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                int width = mIv.getWidth();
//                int height = mIv.getHeight();
//                Log.v(TAG, "width = " + width + "; height = " + height);
//            }
//        });

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myAnimationDrawable.animateRawManuallyFromXML(R.drawable.candy_flash_anim, mIv, new Runnable() {
                    @Override
                    public void run() {
                        Utils.showToast(mContext, "onStart");
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showToast(mContext, "onComplete");
                            }
                        });
                    }
                });
            }
        });

        mPinyinTv = findViewById(R.id.pinyin_tv);
        String hanzi = "我哦我我我哦我饿哦我adbdfa我我哦我我范德萨范德萨撒地方双方都反倒是";
        String[] hanzis = Utils.getFormatHanzi(hanzi);
        String[] pinyins = Utils.getPinyinString(hanzi);
        Log.v(TAG, "hanzis length = " + hanzis.length + "; pinyins length = " + pinyins.length);
        mPinyinTv.setHanzi(hanzis);
        mPinyinTv.setPinyin(pinyins);
        mPinyinTv.setScrollEnable(true);
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        myAnimationDrawable.stopAnim();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}