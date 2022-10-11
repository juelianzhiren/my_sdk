package com.ztq.sdk.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.ztq.sdk.R;
import com.ztq.sdk.glide_test.CircleCrop;
import com.ztq.sdk.glide_test.ProgressInterceptor;
import com.ztq.sdk.glide_test.ProgressListener;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.glide_test.DownloadImageTarget;
import com.ztq.sdk.widget.MyLayoutWithViewTarget;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;

public class GlideActivity extends BaseActivity {
    private static final String TAG = "noahedu.GlideActivity";
    private MyLayoutWithViewTarget myLayoutWithViewTarget;
    private ImageView imageView;
    private Context mContext;

    ProgressDialog progressDialog;
    String url = "http://guolin.tech/book.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);

        mContext = this;
        imageView = (ImageView) findViewById(R.id.image_view);
        myLayoutWithViewTarget = findViewById(R.id.mylayout);
        Log.d(TAG, "imageView scaleType is " + imageView.getScaleType());

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("加载中");

        addListener();
    }

    private void addListener() {
        ProgressInterceptor.addListener(url, new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                progressDialog.setProgress(progress);
            }
        });


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

//                url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
//                Glide.with(mContext)
//                        .load(url)
//                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                        .into(myLayoutWithViewTarget.getTarget());

//                downloadImage(v);

//                loadImage(v);

//                downloadImage1(v);

                loadImage1(v);
            }
        });
    }

    public void loadImage1(View view) {
//        String url = "https://www.baidu.com/img/bd_logo1.png";
//        Glide.with(this)
//                .load(url)
////                .dontTransform()
////                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
////                .centerCrop()
////                .fitCenter()
////                .transform(new CircleCrop(this))
////                .bitmapTransform(new BlurTransformation(this))
////                .bitmapTransform(new GrayscaleTransformation(this))
//                .bitmapTransform(new BlurTransformation(this), new GrayscaleTransformation(this))
//                .into(imageView);


//        Glide.with(this)
//                .load(url)
//                .skipMemoryCache(true)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .into(imageView);

        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        progressDialog.show();
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        progressDialog.dismiss();
                        ProgressInterceptor.removeListener(url);
                    }
                });
    }

    public void downloadImage1(View view) {
        String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
        Glide.with(this)
                .load(url)
                .downloadOnly(new DownloadImageTarget());
    }

    public void loadImage(View view) {
        String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
    }

    public void downloadImage(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://cn.bing.com/az/hprichbg/rb/TOAD_ZH-CN7336795473_1920x1080.jpg";
                    final Context context = getApplicationContext();
                    FutureTarget<File> target = Glide.with(context)
                            .load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                    final File imageFile = target.get();
                    Log.v(TAG, "path = " + imageFile.getPath());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context, imageFile.getPath(), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}