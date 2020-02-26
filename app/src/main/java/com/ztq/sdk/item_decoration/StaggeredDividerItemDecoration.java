package com.ztq.sdk.item_decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

public class StaggeredDividerItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private int interval;

    public StaggeredDividerItemDecoration(Context context, int interval) {
        this.context = context;
        this.interval = interval;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        int position = parent.getChildAdapterPosition(view);
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        // 获取item在span中的下标
        int spanIndex = params.getSpanIndex();
        int interval = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.interval, context.getResources().getDisplayMetrics());
        // 中间间隔
        if (spanIndex % 3 == 0) {
            outRect.left = 0;
        } else {
            // item为奇数位，设置其左间隔为5dp
            outRect.left = interval;
        }
        if (spanIndex % 3 == 2) {
            outRect.right = 0;
        } else {
            // item为奇数位，设置其左间隔为5dp
            outRect.right = interval;
        }
        // 下方间隔
        outRect.bottom = interval;
    }
}