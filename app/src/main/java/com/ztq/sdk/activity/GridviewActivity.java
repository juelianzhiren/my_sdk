package com.ztq.sdk.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;

import com.ztq.sdk.R;
import com.ztq.sdk.adapter.GridviewAdapter;
import com.ztq.sdk.helper.MyHandlerThread;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztq on 2019/10/24.
 */
public class GridviewActivity extends Activity {
    private final String TAG = "noahedu.GridviewActivity";
    private Context mContext;
    private GridView mGridView;
    private GridviewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        mContext = this;
        mGridView = findViewById(R.id.gridview);
        mAdapter = new GridviewAdapter(mContext);
        mGridView.setAdapter(mAdapter);

        final List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        list.add("7");
        list.add("8");
        list.add("9");
        list.add("10");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        list.add("16");
        list.add("17");
        list.add("18");
        list.add("19");
        list.add("20");
        list.add("21");
        list.add("22");
        list.add("23");
        list.add("24");
        list.add("25");
        list.add("26");
        list.add("27");
        list.add("28");
        list.add("29");
        list.add("30");

        mAdapter.addData(list);
    }
}