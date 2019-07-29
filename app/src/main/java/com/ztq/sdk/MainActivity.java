package com.ztq.sdk;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ztq.sdk.model.MyAnimationDrawable;
import com.ztq.sdk.utils.Utils;
import com.ztq.sdk.widget.MyImageView;
import com.ztq.sdk.widget.PinyinTextView;

import java.nio.channels.OverlappingFileLockException;
import java.util.List;

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
        List<String> hanziList = Utils.getFormatHanzi(hanzi);
        List<String> pinyinList = Utils.getPinyinString(hanzi);

        pinyinList.clear();
        pinyinList.add(" wo ");
        pinyinList.add(" o ");
        pinyinList.add(" wo ");
        pinyinList.add(" wo ");
        pinyinList.add(" wo ");
        pinyinList.add(" o ");
        pinyinList.add(" wo ");
        pinyinList.add(" e ");
        pinyinList.add(" o ");
        pinyinList.add(" wo ");
        pinyinList.add("null");
        pinyinList.add("null");
        pinyinList.add("null");
        pinyinList.add("null");
        pinyinList.add("null");
        pinyinList.add("null");
        pinyinList.add(" wo ");
        pinyinList.add(" wo");
        pinyinList.add(" o ");
        pinyinList.add(" wo ");
        pinyinList.add(" wo ");
        pinyinList.add(" fan ");
        pinyinList.add(" de ");
        pinyinList.add(" sha ");
        pinyinList.add(" fan ");
        pinyinList.add(" de ");
        pinyinList.add(" sha ");
        pinyinList.add(" sha ");
        pinyinList.add(" di ");
        pinyinList.add(" fang ");
        pinyinList.add(" shuang ");
        pinyinList.add(" fang ");
        pinyinList.add(" du ");
        pinyinList.add(" fan ");
        pinyinList.add(" dao ");
        pinyinList.add(" shi ");

        Log.v(TAG, "hanzis length = " + hanziList.size() + "; pinyins length = " + pinyinList.size());
        mPinyinTv.setHanziList(hanziList);
        mPinyinTv.setPinyinList(pinyinList);
        mPinyinTv.setmIsPinyinGeneratedByPinyin4jJar(false);
        mPinyinTv.setScrollEnable(false);
        mPinyinTv.setTextSize(60);
        mPinyinTv.setTextColor(Color.BLACK);
        mPinyinTv.setPinyinSize(30);
        mPinyinTv.setPinyinColor(Color.RED);
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