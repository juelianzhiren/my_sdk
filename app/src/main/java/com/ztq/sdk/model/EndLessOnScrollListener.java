package com.ztq.sdk.model;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by wnw on 16-5-26.
 */
public abstract class EndLessOnScrollListener extends RecyclerView.OnScrollListener {
    private static final String TAG = "noahedu.EndLessOnScrollListener";
    //声明一个LinearLayoutManager
    private LinearLayoutManager mLinearLayoutManager;

    private int mLastCompletelyVisibleItem;
    private int mCurrentPage;

    public EndLessOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        com.ztq.sdk.log.Log.v(TAG, "newState = " + newState + "; mLastCompletelyVisibleItem = " + mLastCompletelyVisibleItem + "; mAdapter2.getItemCount() = " + mLinearLayoutManager.getItemCount());
        if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastCompletelyVisibleItem + 1 == mLinearLayoutManager.getItemCount()) {
            mCurrentPage++;
            onLoadMore(mCurrentPage);
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        try {
            mLastCompletelyVisibleItem = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 提供一个抽闲方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     */
    public abstract void onLoadMore(int currentPage);
}