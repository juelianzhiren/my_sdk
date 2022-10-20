package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ztq.sdk.R;
import com.ztq.sdk.widget.HorizontalEx;
import com.ztq.sdk.widget.HorizontalEx2;
import com.ztq.sdk.widget.ListViewEx;

import java.util.ArrayList;
import java.util.List;

public class SlideConflictActivity extends BaseActivity{
    private static final String TAG = "noahedu.SlideConflictActivity";
    private RelativeLayout mMainRL;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_conflict);

        findViews();
        init();
        addListener();
    }

    private void findViews() {
        mMainRL = findViewById(R.id.slide_conflict_rl);
    }

    private void init() {
        mContext = this;
    }

    private void addListener() {
        findViewById(R.id.slide_conflict_1_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainRL.removeAllViews();
                generateData1();
            }
        });

        findViewById(R.id.slide_conflict_2_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainRL.removeAllViews();
            }
        });

        findViewById(R.id.slide_conflict_3_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainRL.removeAllViews();
                generateData3();
            }
        });

        findViewById(R.id.slide_conflict_4_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMainRL.removeAllViews();
            }
        });
    }

    private void generateData1(){
        List<String> data1 = new ArrayList();
        List<String> data2 = new ArrayList();
        List<String> data3 = new ArrayList();
        for(int i = 0; i < 20; i++) {
            data1.add("列表1--" + (i + 1));
            data2.add("列表2--" + (i + 1));
            data3.add("列表3--" + (i + 1));
        }
        showOutHVData(data1, data2, data3);
    }

    public void showOutHVData(List<String> data1, List<String> data2, List<String> data3) {
        ListView listView1 = new ListView(mContext);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, data1);
        listView1.setAdapter(adapter1);

        ListView listView2 = new ListView(mContext);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, data2);
        listView2.setAdapter(adapter2);

        ListView listView3 = new ListView(mContext);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, data3);
        listView3.setAdapter(adapter3);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        HorizontalEx mHorizontalEx = new HorizontalEx(mContext);

        mHorizontalEx.addView(listView1, params);
        mHorizontalEx.addView(listView2, params);
        mHorizontalEx.addView(listView3, params);

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMainRL.addView(mHorizontalEx, params1);
    }

    private void generateData3(){
        List<String> data1 = new ArrayList();
        List<String> data2 = new ArrayList();
        List<String> data3 = new ArrayList();
        for(int i = 0; i < 20; i++) {
            data1.add("列表1--" + (i + 1));
            data2.add("列表2--" + (i + 1));
            data3.add("列表3--" + (i + 1));
        }
        showInnerHVData(data1, data2, data3);
    }

    public void showInnerHVData(List<String> data1, List<String> data2, List<String> data3) {
        HorizontalEx2 mHorizontalEx2 = new HorizontalEx2(mContext);

        ListViewEx listView1 = new ListViewEx(mContext);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, data1);
        listView1.setAdapter(adapter1);
        listView1.setmHorizontalEx2(mHorizontalEx2);

        ListViewEx listView2 = new ListViewEx(mContext);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, data2);
        listView2.setAdapter(adapter2);
        listView2.setmHorizontalEx2(mHorizontalEx2);

        ListViewEx listView3 = new ListViewEx(mContext);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, data3);
        listView3.setAdapter(adapter3);
        listView3.setmHorizontalEx2(mHorizontalEx2);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        mHorizontalEx2.addView(listView1, params);
        mHorizontalEx2.addView(listView2, params);
        mHorizontalEx2.addView(listView3, params);

        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mMainRL.addView(mHorizontalEx2, params1);
    }
}