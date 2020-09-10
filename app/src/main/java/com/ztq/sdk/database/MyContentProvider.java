package com.ztq.sdk.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.ztq.sdk.log.Log;

public class MyContentProvider extends ContentProvider {
    private static final String TAG = "noahedu.MyContentProvider";
    private static final String AUTHORITY = "com.noahedu.sdk";
    public static final Uri DATA_URI = Uri.parse("content://" + AUTHORITY + "/" + MySQLiteOpenHelper.TABLE_MY_SDK);
    private static final int DATA_CODE = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static final String FLAG_INSERT = "insert";
    public static final String FLAG_UPDATE = "update";
    public static final String FLAG_DELETE = "delete";

    static {
        sUriMatcher.addURI(AUTHORITY, MySQLiteOpenHelper.TABLE_MY_SDK, DATA_CODE);
    }

    @Override
    public boolean onCreate() {
        init();
        return false;
    }

    private void init() {
        mContext = getContext();
        mDatabase = new MySQLiteOpenHelper(mContext).getWritableDatabase();
    }

    /**
     * CRUD 的参数是 Uri，根据 Uri 获取对应的表名
     *
     * @param uri
     * @return
     */
    private String getTableName(final Uri uri) {
        String tableName = "";
        int match = sUriMatcher.match(uri);
        switch (match) {
            case DATA_CODE:
                tableName = MySQLiteOpenHelper.TABLE_MY_SDK;
                break;
        }
        return tableName;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        return mDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        long id = mDatabase.insert(tableName, null, values);
        if (id >= 0) {
            Uri insertUri = Uri.withAppendedPath(uri, FLAG_INSERT + "/" + id);     //此处为重点，构造了一个Uri发送出去
            Log.d(TAG, "--insertUrl:" + insertUri);
            mContext.getContentResolver().notifyChange(insertUri, null);
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int count = mDatabase.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            String rowId = "";
            if (null != selectionArgs) {
                rowId = selectionArgs[0];
            }
            Uri deleteUri = Uri.withAppendedPath(uri, FLAG_DELETE + "/" + rowId);//此处为重点，构造了一个Uri发送出去
            Log.d(TAG, "--deleteUri:" + deleteUri);
            mContext.getContentResolver().notifyChange(deleteUri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int count = mDatabase.update(tableName, values, selection, selectionArgs);
        if (count > 0) {
            String rowId = "";
            if (null != selectionArgs) {
                rowId = selectionArgs[0];
            }
            Uri updateUri = Uri.withAppendedPath(uri, FLAG_UPDATE + "/" + rowId);      //此处为重点，构造了一个Uri发送出去
            Log.d(TAG, "--updateUri:" + updateUri);
            mContext.getContentResolver().notifyChange(updateUri, null);
        }
        return count;
    }
}