package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ztq.sdk.R;
import com.ztq.sdk.adapter.RecyclerViewAdapter;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;

import java.util.ArrayList;
import java.util.List;

public class XRecyclerViewActivity extends BaseActivity {
    private static final String TAG = "noahedu.XRecyclerViewActivity";
    private Context mContext;
    private XRecyclerView mXRecyclerView;
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xrecyclerview);

        mContext = this;
        findViews();
        init();
        addListener();
    }

    private void findViews() {
        mXRecyclerView = findViewById(R.id.xrecycler_view);
    }

    private void init() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add("这是第" + i + "个测试");
        }

        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mXRecyclerView.setLayoutManager(manager);
        mXRecyclerView.setAnimation(null);

        mAdapter = new RecyclerViewAdapter(mContext, list, false);
        mXRecyclerView.setAdapter(mAdapter);
        mXRecyclerView.setPullRefreshEnabled(true);
        mXRecyclerView.setLoadingMoreEnabled(true);
    }

    private void addListener() {
        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh(XRecyclerView xRecyclerView) {
                Log.v(TAG, "onRefresh");
                MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXRecyclerView.refreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(XRecyclerView xRecyclerView) {
                Log.v(TAG, "onLoadMore");
                MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mXRecyclerView.loadMoreComplete();
                    }
                }, 2000);
            }
        });
    }
}