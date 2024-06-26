package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

import com.ztq.sdk.R;
import com.ztq.sdk.adapter.TimelineAdapter;
import com.ztq.sdk.entity.TimelineItem;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.NetworkUtils;
import com.ztq.sdk.utils.Utils;
import com.ztq.sdk.widget.MySeekbar;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

public class TimelineActivity extends BaseActivity implements CancelAdapt {
    private static final String TAG = "noahedu.TimelineActivity";
    private ListView mListView;
    private TimelineAdapter mAdapter;
    private Context mContext;
    private MySeekbar mySeekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        mContext = this;
        mListView = findViewById(R.id.listview);
        mAdapter = new TimelineAdapter(mContext);
        mListView.setAdapter(mAdapter);

        List<TimelineItem> list = new ArrayList<TimelineItem>();
        TimelineItem item = new TimelineItem();
        item.setContent("打包成功");
        item.setDateInfo("2016-01-30 19:17:50");
        list.add(item);
        item = new TimelineItem();
        item.setContent("扫描员已经扫描");
        item.setDateInfo("2016-01-30 19:17:50");
        list.add(item);
        item = new TimelineItem();
        item.setContent("您的订单在京东【深圳南山区】分拣成功");
        item.setDateInfo("2016-01-30 19:18:52");
        list.add(item);
        item = new TimelineItem();
        item.setContent("感谢您在京东上购物，欢迎您再次光临");
        item.setDateInfo("2016-02-01 13:30:20");
        list.add(item);
        mAdapter.addData(list);

        mySeekbar = findViewById(R.id.my_seekbar);
        mySeekbar.setTextSize(Utils.dp2px(mContext, 10));
        List<String> wordList = new ArrayList<>();
        wordList.add("0.75X");
        wordList.add("1.0X");
        wordList.add("1.25X");
//        wordList.add("1.5X");
        mySeekbar.setCircleRadius(Utils.dp2px(mContext, 10));
        mySeekbar.setWordList(wordList);
        mySeekbar.setSelectListener(new MySeekbar.SelectListener() {
            @Override
            public void select(int index) {
                Log.v(TAG, "index = " + index);
            }
        });

        MyHandlerThread.postToWorker1(new Runnable() {
            @Override
            public void run() {
                boolean isNetAvailable = NetworkUtils.isNetAvailable(mContext);
                boolean isWeakNetwork = NetworkUtils.isWeakNetwork(mContext);
                Log.v(TAG, "isNetAvailable = " + isNetAvailable + "; isWeakNetwork = " + isWeakNetwork);
            }
        });
    }
}