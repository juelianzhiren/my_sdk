package com.ztq.sdk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.ztq.sdk.R;
import com.ztq.sdk.model.MyAnimationDrawable;
import com.ztq.sdk.utils.SignMd5;
import com.ztq.sdk.utils.Utils;
import com.ztq.sdk.widget.CircleProgressBar;
import com.ztq.sdk.widget.MyImageView;
import com.ztq.sdk.widget.PinyinTextView;

import java.util.List;

/**
 * 这是测试的Activity
 */
public class MainActivity extends Activity {
    private final String TAG = "noahedu.MainActivity";
    private Context mContext;
    private MyImageView mIv;
    private Button mBtn;
    private MyAnimationDrawable myAnimationDrawable;
    private PinyinTextView mPinyinTv;
    private CircleProgressBar mCircleProgressBar;
    private boolean mIsPause = false;
    private int mProgress = 0;

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
        String hanzi = "我哦我我我哦我饿哦我adbdfa我我哦\n我我范德萨范德萨撒地方双方都反倒是为我离我而哦我我饿哦我我饿哦我嗯哦哦饿哦沃尔沃浪费\n了打算理发静安寺法拉第十六大是否发大水";
        List<String> hanziList = Utils.getFormatHanzi(hanzi);

        List<String> pinyinList = Utils.getPinyinString(hanzi);
//        pinyinList.clear();
//        pinyinList.add(" wo ");
//        pinyinList.add(" o ");
//        pinyinList.add(" wo ");
//        pinyinList.add(" wo ");
//        pinyinList.add(" wo ");
//        pinyinList.add(" o ");
//        pinyinList.add(" wo ");
//        pinyinList.add(" e ");
//        pinyinList.add(" o ");
//        pinyinList.add(" wo ");
//        pinyinList.add("null");
//        pinyinList.add("null");
//        pinyinList.add("null");
//        pinyinList.add("null");
//        pinyinList.add("null");
//        pinyinList.add("null");
//        pinyinList.add(" wo ");
//        pinyinList.add(" wo");
//        pinyinList.add(" o ");
//        pinyinList.add("null");
//        pinyinList.add(" wo ");
//        pinyinList.add(" wo ");
//        pinyinList.add(" fan ");
//        pinyinList.add(" de ");
//        pinyinList.add(" sha ");
//        pinyinList.add(" fan ");
//        pinyinList.add(" de ");
//        pinyinList.add(" sha ");
//        pinyinList.add(" sha ");
//        pinyinList.add(" di ");
//        pinyinList.add(" fang ");
//        pinyinList.add(" shuang ");
//        pinyinList.add(" fang ");
//        pinyinList.add(" du ");
//        pinyinList.add(" fan ");
//        pinyinList.add(" dao ");
//        pinyinList.add(" shi ");

        Log.v(TAG, "hanzis length = " + hanziList.size() + "; pinyins length = " + pinyinList.size());
        for(int i = 0; i < hanziList.size(); i++) {
            Log.v(TAG, "hanzi 111 " + i + ": " + hanziList.get(i).equals("\n"));
        }
        mPinyinTv.setHanziList(hanziList);
        mPinyinTv.setPinyinList(pinyinList);
        mPinyinTv.organzieData();
        mPinyinTv.setIsPinyinComposeOf7Chars(true);
        mPinyinTv.setScrollEnable(true);
        mPinyinTv.setTextSize(80);
        mPinyinTv.setTextColor(Color.BLACK);
        mPinyinTv.setPinyinSize(40);
        mPinyinTv.setPinyinColor(Color.RED);
//        mPinyinTv.setIsShowPinyin(false);
        findViewById(R.id.jump_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DemoActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.jump_big_picuture_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BigPictureActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.jump_to_lyrics_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LyricsActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.jump_to_gridview_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GridviewActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.jump_to_zoom_image_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ZoomImageActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.overdraw_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OverdrawActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.ndk_demo_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NdkDemoActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.mapview_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapViewActivity.class);
                startActivity(intent);
            }
        });

        mIv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.v(TAG, "iv, width = " + mIv.getWidth() + "; height = " + mIv.getHeight());
                mIv.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mCircleProgressBar = findViewById(R.id.cbr);
        mCircleProgressBar.setIsShowTextProgress(true);

        Log.v(TAG, "AppStore.apk md5 value is " + SignMd5.getMd5(this));

        long maxMemory = Runtime.getRuntime().maxMemory();
        Log.v(TAG, "maxMemory = " + maxMemory / 1024.0 / 1024.0 + "m");

        Log.v(TAG, "manufacture = " + Build.MANUFACTURER);
        //华为角标的实现
        if (Build.MANUFACTURER.equalsIgnoreCase("huawei")) {
            Bundle extra = new Bundle();
            extra.putString("package", "com.ztq.sdk");
            extra.putString("class", "com.ztq.sdk.activity.MainActivity");
            extra.putInt("badgenumber", 3);
//            mContext.getContentResolver().call(Uri.parse("content://com.huawei.android.launcher.settings/badge/"), "change_badge", null, extra);
        }
        DisplayMetrics dm = getResources().getDisplayMetrics();
        Log.v(TAG, "screen width = " + dm.widthPixels + "; height = " + dm.heightPixels + "; densityDpi = " + dm.densityDpi + "; xdpi = " + dm.xdpi + "; ydpi = " + dm.ydpi + "; scaledDensity = " +dm.scaledDensity);
        Drawable drawable = getResources().getDrawableForDensity(R.drawable.ic_pause, 160);
        Log.v(TAG, "drawable = " + drawable);

        Matrix matrix = new Matrix();
        Log.v(TAG, "matrix.toString = " + matrix.toString() + "; matrix.toShortString = " + matrix.toShortString());
        matrix.setValues(new float[]{1, 2, 3, 1, 2, 3, 1, 2, 3});
//        Log.v(TAG, "matrix.toString = " + matrix.toString() + "; matrix.toShortString = " + matrix.toShortString());
        matrix.postTranslate(-1, -2);
        Log.v(TAG, "matrix.toString = " + matrix.toString() + "; matrix.toShortString = " + matrix.toShortString());
        matrix.postScale(-1, -2);
        Log.v(TAG, "matrix.toString = " + matrix.toString() + "; matrix.toShortString = " + matrix.toShortString());
        RectF r = new RectF(-45, 6, 150, 50);
        matrix.mapRect(r);
        Log.d(TAG, "matrix -r.left = " + r.left + ", right = " + r.right + ", top = " + r.top + ", bottom = " + r.bottom);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                // 子线程中弹出toast
                Toast.makeText(mContext, "提示一下！", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }).start();

        findViewById(R.id.jump_to_animation_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AnimationActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.petals_round_view_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PetalsRoundActivity.class);
                startActivity(intent);
            }
        });

        String str1 = com.ztq.sdk.data_structure_and_algorithm.Utils.getFirstLetter2("aaccdeff");
        Log.v(TAG, "str1 = " + str1);

        int test[] = { 7, 5, 6, 4 };
        int count = com.ztq.sdk.data_structure_and_algorithm.Utils.getInversePairsNumber(test);
        Log.v(TAG, "inversePairsNum = " + count);

        int a[] = new int[] { 60, 55, 48, 37, 10, 90, 84, 36, -1};
//        com.ztq.sdk.data_structure_and_algorithm.Utils.bubbleSort(a);
        com.ztq.sdk.data_structure_and_algorithm.Utils.quickSort(a);
//        com.ztq.sdk.data_structure_and_algorithm.Utils.simpleChooseSort(a);
//        com.ztq.sdk.data_structure_and_algorithm.Utils.directInsertSort(a);
//        com.ztq.sdk.data_structure_and_algorithm.Utils.shellSort(a);
//        com.ztq.sdk.data_structure_and_algorithm.Utils.heapSort(a);

        int[] b = new int[] { 1, 2, 3, 3, 3, 3, 4, 5 };
        com.ztq.sdk.data_structure_and_algorithm.Utils.getNumberOfK(b, 210);

        com.ztq.sdk.data_structure_and_algorithm.Utils.getLastRemaining(5,1);

        addListener();
    }

    private void addListener() {
        findViewById(R.id.recyclerview_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RecyclerViewActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.timeline_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TimelineActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public Resources getResources() {
        return super.getResources();
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
        mIsPause = !mIsPause;
        mProgress++;
        mCircleProgressBar.setmIsPause(mIsPause);
        mCircleProgressBar.setCurrentProgress(mProgress);
    }
}