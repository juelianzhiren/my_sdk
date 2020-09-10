package com.ztq.sdk.activity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztq.sdk.R;
import com.ztq.sdk.database.Entity;
import com.ztq.sdk.database.MyContentProvider;
import com.ztq.sdk.database.MySQLiteOpenHelper;
import com.ztq.sdk.log.Log;
import com.ztq.sdk.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 * ContentObserver demo的activity
 */
public class ContentObserverActivity extends BaseActivity {
    private static final String TAG = "noahedu.ContentObserverActivity";
    private Context mContext;
    private RecyclerView mRecyclerView;
    private List<Entity> mContentList;
    private int mCount = 0;
    private RecyclerViewAdapter mAdapter;

    private static final int OPERATION_INSERT = 1;
    private static final int OPERATION_UPDATE = 2;
    private static final int OPERATION_DELETE = 3;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private ContentObserver mContentObserver = new ContentObserver(mHandler) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.v(TAG, "onChange");
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Log.v(TAG, "onChange" + "; uri = " + uri);
            int operation = getOperationTypeByUri(uri);//根据Uri获取动作类型(增加、删除等)
            String mCurrentMessageId = getIdByUri(uri);      //根据Uri获取改变的mID
            Log.d(TAG, "--operation:" + operation + "--mCurrentMessageId:" + mCurrentMessageId);
            doRefreshByOperation(operation, mCurrentMessageId);  //执行数据更新操作
        }
    };

    //根据Uri获取动作类型
    private int getOperationTypeByUri(Uri uri) {
        int operationType = -1;
        String strUri = uri.toString();
        int endIndex = strUri.lastIndexOf("/");
        int beginIndex = endIndex;
        while (beginIndex >= 1) {
            String str = strUri.substring(beginIndex - 1, beginIndex);
            if (str.equals("/")) {
                break;
            }
            beginIndex--;
        }
        String type = strUri.substring(beginIndex, endIndex);
        Log.d(TAG, "--getOperationTypeByUri--type:" + type);
        if (!TextUtils.isEmpty(type)) {
            if (MyContentProvider.FLAG_INSERT.equals(type)) {
                operationType = OPERATION_INSERT;
            } else if (MyContentProvider.FLAG_UPDATE.equals(type)) {
                operationType = OPERATION_UPDATE;
            } else if (MyContentProvider.FLAG_DELETE.equals(type)) {
                operationType = OPERATION_DELETE;
            }
        }
        Log.d(TAG, "--getOperationTypeByUri--operationType:" + operationType);
        return operationType;
    }

    //根据Uri获取改变的mID
    private String getIdByUri(Uri uri) {
        String id = "";
        if (null == uri) {
            Log.e(TAG, "--getIdbyUri--uri is null, return");
            return id;
        }
        String strUri = uri.toString();
        int lastIndex = strUri.lastIndexOf("/");
        id = strUri.substring(lastIndex + 1);
        Log.d(TAG, "--getIdByUri--id:" + id);
        return id;
    }

    //根据数据库动作类型，执行不同的操作（此处实现的是聊天页面listView异步刷新单个Item）
    private void doRefreshByOperation(int operation, String id) {
        if (mContext == null) {
            return;
        }
        switch (operation) {
            case OPERATION_INSERT: {
                String[] projections = new String[]{ MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_NAME };
                String selection = "rowid = ?";
                String[] selectionArgs = new String[]{ id };
                Cursor cursor = mContext.getContentResolver().query(MyContentProvider.DATA_URI, projections, selection, selectionArgs, null);
                if (cursor != null) {
                    if (cursor.moveToNext()) {
                        Entity entity = new Entity();
                        int id1 = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COLUMN_ID));
                        String name = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COLUMN_NAME));
                        entity.setId(id1);
                        entity.setName(name);
                        mContentList.add(entity);
                        mAdapter.notifyItemInserted(mContentList.size() - 1);
                    }
                    cursor.close();
                }
            }
            break;
            case OPERATION_UPDATE: {
                String[] projections = new String[]{ MySQLiteOpenHelper.COLUMN_NAME };
                String selection = MySQLiteOpenHelper.COLUMN_ID + " = ?";
                String[] selectionArgs = new String[]{ id };
                Cursor cursor = mContext.getContentResolver().query(MyContentProvider.DATA_URI, projections, selection, selectionArgs, null);
                String name = "";
                int index = -1;
                if (cursor != null) {
                    if (cursor.moveToNext()) {
                        name = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COLUMN_NAME));
                    }
                    cursor.close();
                }
                if (!Utils.isNullOrNil(name)) {
                    for(int i = 0; i < mContentList.size(); i++) {
                        Entity entity = mContentList.get(i);
                        if (entity != null) {
                            if (Utils.getInt(id) == entity.getId()) {
                                entity.setName(name);
                                index = i;
                                break;
                            }
                        }
                    }
                    // 单条刷新逻辑
                    if (index != -1) {
                        mAdapter.notifyItemChanged(index, 1);
                    }
                }
            }
                break;
            case OPERATION_DELETE: {
                int index = -1;
                for (int i = 0; i < mContentList.size(); i++) {
                    Entity entity = mContentList.get(i);
                    if (entity != null) {
                        if (Utils.getInt(id) == entity.getId()) {
                            index = i;
                            break;
                        }
                    }
                }
                if (index != -1) {
                    mAdapter.notifyItemRemoved(mContentList.size() - 1);
                    mContentList.remove(index);
                }
            }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_observer);

        findViews();
        init();
        addListener();
    }

    private void findViews() {
        mRecyclerView = findViewById(R.id.activity_content_observer_recycler_view);
    }

    private void init() {
        mContext = this;
        mContentList = new ArrayList<>();
        mContentList = getData();
        mAdapter = new RecyclerViewAdapter(mContext, mContentList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        ContentResolver cr = getContentResolver();
        cr.registerContentObserver(MyContentProvider.DATA_URI, true, mContentObserver);
    }

    private List<Entity> getData() {
        List<Entity> list = new ArrayList<>();
        Cursor cursor = mContext.getContentResolver().query(MyContentProvider.DATA_URI, null, null, null, null);
        if (cursor != null) {
            mCount = cursor.getCount();
            while (cursor.moveToNext()) {
                Entity entity = new Entity();
                int id = cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(MySQLiteOpenHelper.COLUMN_NAME));
                entity.setId(id);
                entity.setName(name);
                list.add(entity);
            }
            cursor.close();
        }
        return list;
    }

    private void addListener() {
        findViewById(R.id.activity_content_observer_insert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCount++;
                ContentValues values = new ContentValues();
                values.put(MySQLiteOpenHelper.COLUMN_ID, mCount);
                values.put(MySQLiteOpenHelper.COLUMN_NAME, mCount + "");
                Log.v(TAG, "insert, mCount = " + mCount);
                mContext.getContentResolver().insert(MyContentProvider.DATA_URI, values);
            }
        });
        findViewById(R.id.activity_content_observer_update_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                int newValue = new Random().nextInt(100);
                values.put(MySQLiteOpenHelper.COLUMN_NAME, newValue + "");
                String where = MySQLiteOpenHelper.COLUMN_ID + " = ?";
                String[] selectionArgs = new String[]{mCount + ""};
                Log.v(TAG, "update, mCount = " + mCount + "; newValue = " + newValue);
                mContext.getContentResolver().update(MyContentProvider.DATA_URI, values, where, selectionArgs);
            }
        });
        findViewById(R.id.activity_content_observer_delete_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String where = MySQLiteOpenHelper.COLUMN_ID + " = ?";
                String[] selectionArgs = new String[]{ mCount + "" };
                Log.v(TAG, "delete, mCount = " + mCount );
                mContext.getContentResolver().delete(MyContentProvider.DATA_URI, where, selectionArgs);
                mCount--;
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mContentObserver != null) {
            getContentResolver().unregisterContentObserver(mContentObserver);
        }
        super.onDestroy();
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
        private static final String TAG = "noahedu.RecyclerViewAdapter";
        private Context mContext;
        private List<Entity> list;
        private View view;

        //构造方法，传入数据
        public RecyclerViewAdapter(Context context, List<Entity> list) {
            this.mContext = context;
            this.list = list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.v(TAG, "onCreateViewHolder, viewType = " + viewType);
            //创建ViewHolder，返回每一项的布局
            view = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
            String extras = "";
            for(int i = 0; i < payloads.size(); i++) {
                extras += payloads.get(i).toString() + ",";
            }
            Log.v(TAG, "onBindViewHolder, position = " + position + "; extras = " + extras + "; " + holder.itemView);
            //将数据和控件绑定
            holder.textView.setText(list.get(position).getName());
        }

        @Override
        public int getItemCount() {
            //返回Item总条数
            return list.size();
        }

        //内部类，绑定控件
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.item_recyclerview_tv);
            }
        }
    }
}