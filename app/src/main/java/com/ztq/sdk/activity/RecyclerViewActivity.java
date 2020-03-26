package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.ztq.sdk.R;
import com.ztq.sdk.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * RecyclerViewActivity
 *
 * @author ztq
 */
public class RecyclerViewActivity extends BaseActivity implements CancelAdapt{
    private static final String TAG = "noahedu.RecyclerViewActivity";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        mContext = this;
        initViewAndVariables();
        addListener();
    }

    private void initViewAndVariables() {
        mRecyclerView = findViewById(R.id.recycler_view);
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("这是第" + i + "个测试");
        }
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        GridLayoutManager manager1 = new GridLayoutManager(mContext,2);
        manager1.setOrientation(GridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager manager2 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //解决item跳动
        manager2.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(manager2);
        mRecyclerView.setAnimation(null);
//        mRecyclerView.addItemDecoration(new StaggeredDividerItemDecoration(mContext, 5));
        mAdapter = new RecyclerViewAdapter(mContext, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addListener() {

    }
}