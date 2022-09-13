package com.ztq.sdk.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.ImageView;

import com.ztq.sdk.R;
import com.ztq.sdk.log.Log;

import java.io.InputStream;

/**
 * Created by ztq
 */
public class FlowLayoutActivity extends BaseActivity {
    private static final String TAG = "noahedu.FlowLayoutActivity";
    ImageView iv;
    private LruCache<String, Bitmap> bitmapLruCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow_layout);

        iv = findViewById(R.id.flow_layout_iv);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_pic_1);
        float xdpi = getResources().getDisplayMetrics().xdpi;
        float ydpi = getResources().getDisplayMetrics().ydpi;
        Log.v(TAG, "bitmap size is " + bitmap.getAllocationByteCount() + "; bitmap.getByteCount() = " + bitmap.getByteCount() + "; width = " + bitmap.getWidth() + "; height = " + bitmap.getHeight() + "; xdpi = " + xdpi + "; ydpi = " + ydpi);
        iv.setImageBitmap(bitmap);

        try {
            InputStream inputStream = getAssets().open("ic_pic_1.jpg");
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.v(TAG, "asset bitmap size is " + bitmap.getAllocationByteCount() + "; width = " + bitmap.getWidth() + "; height = " + bitmap.getHeight() + "; xdpi = " + xdpi + "; ydpi = " + ydpi);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int cacheSize = 20 * 1024 * 1024;
        bitmapLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getAllocationByteCount();
            }
        };
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            bitmapLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String key) {
        return bitmapLruCache.get(key);
    }
}