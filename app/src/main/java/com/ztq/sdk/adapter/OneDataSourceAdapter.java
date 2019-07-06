package com.ztq.sdk.adapter;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 只有一个数据源的adapter
 */
public abstract class OneDataSourceAdapter<T> extends BaseAdapter {
    private static final String TAG = "childedu.OneDataSourceAdapter";

    //数据源
    private List<T> dataSource = new ArrayList<T>();

    /**
     * @description: 添加数据
     * @param data   数据为T
     * @return void
     */
    public void addData(T data) {
        dataSource.add(data);
        notifyDataSetChanged();
    }

    /**
     * @description: 添加数据
     * @param data   数据为List<T>
     * @return void
     */
    public void addData(List<T> data){
        dataSource.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * @description: 添加数据到索引位置
     * @param data   数据为T
     * @return void
     */
    public void addData(int index, T data){
        dataSource.add(index, data);
        notifyDataSetChanged();
    };

    /**
     * @description: 添加数据到索引位置
     * @param data   数据为List<T>
     * @return void
     */
    public void addData(int index, List<T> data){
        dataSource.addAll(index, data);
        notifyDataSetChanged();
    };

    /**
     * @description: 删除索引位置index的数据
     * @param index
     * @return void
     */
    public void remove(int index){
        dataSource.remove(index);
        notifyDataSetChanged();
    };

    /**
     * @description: 清空list数据
     * @return void
     */
    public void clear() {
        dataSource.clear();
        notifyDataSetChanged();
    }

    /**
     * @description: 获取数据源
     * @return
     * @return List<T>
     */
    public List<T> getDataSource() {
        return dataSource;
    }
}