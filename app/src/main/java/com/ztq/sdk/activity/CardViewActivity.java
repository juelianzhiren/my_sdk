package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;

import com.ztq.sdk.R;

public class CardViewActivity extends BaseActivity {
    private static final String TAG = "noahedu.CardViewActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardview);

        mContext = this;
        findViews();
        init();
        addListener();
    }

    private void findViews() {

    }

    private void init() {

    }

    private void addListener() {

    }
}