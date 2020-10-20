package com.ztq.sdk.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.ztq.sdk.R;
import com.ztq.sdk.adapter.RecyclerViewAdapter;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.model.EndLessOnScrollListener;

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
    private RecyclerView mRecyclerView1;
    private RecyclerViewAdapter mAdapter1;
    private List<String> mList;

    private RecyclerView mRecyclerView2;
    private SwipeRefreshLayout mSwipeRefreshLayout2;
    private RecyclerViewAdapter mAdapter2;
    private EndLessOnScrollListener mEndLessOnScrollListener;

    private List<String> list;
    private LinearLayoutManager manager2;
    private int mLastCompletelyVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        mContext = this;
        initViewAndVariables();
        addListener();
    }

    private void initViewAndVariables() {
        mRecyclerView1 = findViewById(R.id.recycler_view1);
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mList.add("这是第" + i + "个测试");
        }
//        LinearLayoutManager manager1 = new LinearLayoutManager(mContext);
//        manager1.setOrientation(LinearLayoutManager.VERTICAL);
        //RecyclerView.LayoutManager manager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
//        GridLayoutManager manager1 = new GridLayoutManager(mContext,2);
//        manager1.setOrientation(GridLayoutManager.VERTICAL);
        StaggeredGridLayoutManager manager1 = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //解决item跳动
        manager1.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView1.setLayoutManager(manager1);
        mRecyclerView1.setAnimation(null);
//        mRecyclerView.addItemDecoration(new StaggeredDividerItemDecoration(mContext, 5));
        mAdapter1 = new RecyclerViewAdapter(mContext, mList, true);
        mRecyclerView1.setAdapter(mAdapter1);

        mRecyclerView2 = findViewById(R.id.recycler_view2);
        mSwipeRefreshLayout2 = findViewById(R.id.srl);
        manager2 = new LinearLayoutManager(mContext);
        manager2.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView2.setLayoutManager(manager2);
        mRecyclerView2.setAnimation(null);
//        mRecyclerView.addItemDecoration(new StaggeredDividerItemDecoration(mContext, 5));
        list = new ArrayList<>();
        list.addAll(mList);
        mAdapter2 = new RecyclerViewAdapter(mContext, list, false);
        mRecyclerView2.setAdapter(mAdapter2);
        mEndLessOnScrollListener = new EndLessOnScrollListener(manager2) {
            @Override
            public void onLoadMore(int currentPage) {
                Log.v(TAG, "onLoadMore");
                MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int count = mAdapter2.getItemCount();
                        for(int i = count; i < count + 10; i++) {
                            list.add("这是第" + i + "个测试");
                        }
                        mAdapter2.notifyDataSetChanged();
                        mSwipeRefreshLayout2.setRefreshing(false);
                    }
                }, 1000);
            }
        };
    }

    private void addListener() {
//        mRecyclerView2.addOnScrollListener(mEndLessOnScrollListener);
        mSwipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v(TAG, "onRefresh");
                MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        list.clear();
                        list.addAll(mList);
                        mAdapter2.notifyDataSetChanged();
                        mSwipeRefreshLayout2.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        mRecyclerView2.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.v(TAG, "newState = " + newState + "; mLastCompletelyVisibleItem = " + mLastCompletelyVisibleItem + "; mAdapter2.getItemCount() = " + mAdapter2.getItemCount() + "; flag = " + (recyclerView == mRecyclerView2));
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastCompletelyVisibleItem + 1 == mAdapter2.getItemCount()) {
                    Log.v(TAG, "onLoadMore");
                    mAdapter2.setIsShowFooter(true);
                    final int count = mAdapter2.getItemCount();
                    mAdapter2.notifyItemChanged(count - 1);
                    MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int count1 = mAdapter2.getItemCount();
                            for(int i = count1; i < count1 + 10; i++) {
                                list.add("这是第" + i + "个测试");
                            }
                            mAdapter2.setIsShowFooter(false);
                            for(int i = count1 - 1; i < list.size(); i++) {
                                mAdapter2.notifyItemChanged(i);
                            }
                            mSwipeRefreshLayout2.setRefreshing(false);
                        }
                    },1000);
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView,dx, dy);
                mLastCompletelyVisibleItem = manager2.findLastCompletelyVisibleItemPosition();
            }
        });
    }
}