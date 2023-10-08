package com.ztq.sdk.jetpack_demo.navigation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.ztq.sdk.jetpack_demo.R;

public class NavigationActivity extends FragmentActivity {
    private static final String TAG = "noahedu.NavigationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
    }
}