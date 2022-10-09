package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ztq.sdk.R;
import com.ztq.sdk.widget.MyLayoutWithViewTarget;

public class GlideActivity extends BaseActivity {
    private static final String TAG = "noahedu.GlideActivity";
    private MyLayoutWithViewTarget myLayoutWithViewTarget;
    private ImageView imageView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);
        mContext = this;
        imageView = (ImageView) findViewById(R.id.image_view);
        myLayoutWithViewTarget = findViewById(R.id.mylayout);
        addListener();
    }

    private void addListener() {
        findViewById(R.id.glide_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://cn.bing.com/az/hprichbg/rb/Dongdaemun_ZH-CN10736487148_1920x1080.jpg";

//                Glide.with(mContext)
//                        .load(url)
//                        .placeholder(R.drawable.ic_loading)
//                        .error(R.drawable.ic_load_fail)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(false)
//                        .override(541, 304)
//                        .into(imageView);

//                int resId = R.drawable.abc;
//                Glide.with(mContext)
//                        .load(resId)
//                        .asBitmap()
//                        .placeholder(R.drawable.ic_loading)
//                        .error(R.drawable.ic_load_fail)
//                        .diskCacheStrategy(DiskCacheStrategy.NONE)
//                        .skipMemoryCache(true)
//                        .into(imageView);

                url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
                Glide.with(mContext)
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(myLayoutWithViewTarget.getTarget());
            }
        });
    }
}