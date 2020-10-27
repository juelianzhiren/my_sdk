package com.ztq.sdk.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ztq.sdk.R;
import com.ztq.sdk.adapter.OneDataSourceForRecyclerAdapter;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * BRVAH的demo，BRVAH这个开源库不断在更新完善，而且里面有很多实用的功能点
 */
public class BaseRecyclerViewAdapterHelperActivity extends BaseActivity {
    private static final String TAG1 = "noahedu.BaseRecyclerViewAdapterHelperActivity";
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<String> mDatas;
    private HomeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_recyclerview_adapter_helper);

        findViews();
        init();
        addListener();
    }

    protected void initData() {
        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++) {
            mDatas.add("" + (char) i);
        }
    }

    private void findViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_recyclerview_adapter_helper_srl);
    }

    private void init() {
        initData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //适配器参数：item布局、列表数据源
        mAdapter = new HomeAdapter(R.layout.item_base_recyclerview_adapter_helper);
        mAdapter.addHeaderView(getHeaderView());
        mAdapter.addFooterView(getFooterView());
        //设置数据
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addData(mDatas);
        //动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
    }

    private View getHeaderView() {
        return LayoutInflater.from(this).inflate(R.layout.base_recyclerview_adapter_helper_headerview, null);
    }

    private View getFooterView() {
        return LayoutInflater.from(this).inflate(R.layout.base_recyclerview_adapter_helper_footerview, null);
    }

    private void addListener() {
        //短按item
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG1, "onItemClick: ");
                Toast.makeText(BaseRecyclerViewAdapterHelperActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
        //长按item
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG1, "onItemLongClick: ");
                Toast.makeText(BaseRecyclerViewAdapterHelperActivity.this, "onItemLongClick" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //短按item子控件
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG1, "OnItemChildClickListener, position = " + position);
                Toast.makeText(BaseRecyclerViewAdapterHelperActivity.this, "OnItemChildClickListener, " + position, Toast.LENGTH_SHORT).show();
            }
        });
        //长按item子控件
        mAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG1, "OnItemChildLongClickListener, position = " + position);
                Toast.makeText(BaseRecyclerViewAdapterHelperActivity.this, "OnItemChildLongClickListener, " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addData(mDatas);
                        mAdapter.loadMoreComplete();
                    }
                }, 2000);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.clear();
                        mAdapter.addData(mDatas);
                        mSwipeRefreshLayout.setRefreshing(false);                    }
                }, 2000);
            }
        });
    }

    //不用框架的适配器
//    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.item_home, parent, false));
//            return holder;
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, int position) {
//            holder.tv.setText(mDatas.get(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mDatas.size();
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//
//            TextView tv;
//
//            public MyViewHolder(View view) {
//                super(view);
//                tv = (TextView) view.findViewById(R.id.id_num);
//            }
//        }
//    }

    class HomeAdapter extends OneDataSourceForRecyclerAdapter<String, BaseViewHolder> {

        /**
         * 构造方法
         * @param layoutResId：
         */
        public HomeAdapter(int layoutResId) {
            super(layoutResId);
        }

        /**
         * 设置数据
         *
         * @param helper ：holder
         * @param item   :item的数据
         */
        @Override
        protected void convert(BaseViewHolder helper, String item) {
            Log.d(TAG1, "convert, helper = " + helper + "; postion = " + helper.getAdapterPosition());
            helper.setText(R.id.id_num, item);//item布局的控件id、item数据
            helper.setText(R.id.id_text, "第" + helper.getAdapterPosition() + "个数据");
            //item布局的控件id、item数据

            helper.addOnClickListener(R.id.id_num);
            helper.addOnLongClickListener(R.id.id_text);

            //getLayoutPosition() 获取当前item的position
            if (helper.getAdapterPosition() % 2 == 0) {
                helper.setTextColor(R.id.id_num, Color.RED);
            } else {
                helper.setTextColor(R.id.id_num, Color.YELLOW);
            }
        }
    }
}