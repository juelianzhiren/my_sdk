package com.ztq.sdk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.ztq.sdk.R;

/**
 * Created by ztq on 2019/11/6.
 */
public class OverdrawActivity extends Activity {
    private final String TAG = "noahedu.OverdrawActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdraw);
//        getWindow().setBackgroundDrawable(null);
    }
}