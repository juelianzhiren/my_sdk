package com.ztq.sdk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ztq.sdk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztq on 2019/11/14.
 */
public class MySQLiteBaseService {
    private final String TAG = "noahedu.MySQLiteBaseService";
    private static MySQLiteBaseService mInstance;
    private Context mContext;
    private MySQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;

    private MySQLiteBaseService(Context context) {
        mContext = context.getApplicationContext();
        mSQLiteOpenHelper = new MySQLiteOpenHelper(context.getApplicationContext());
    }

    public static MySQLiteBaseService getInstance(Context context) {
        if (context == null) {
            return null;
        }
        if(mInstance == null) {
            synchronized (MySQLiteBaseService.class) {
                if(mInstance == null) {
                    mInstance = new MySQLiteBaseService(context);
                }
            }
        }
        return mInstance;
    }

    /**
     *
     * @param name
     * @return
     */
    public boolean queryData(String name){
        if (Utils.isNullOrNil(name)) {
            return false;
        }
        mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        Cursor cursor = mSQLiteDatabase.query(MySQLiteOpenHelper.TABLE_MY_SDK, new String[]{ MySQLiteOpenHelper.COLUMN_ID }, MySQLiteOpenHelper.COLUMN_NAME + " = ?", new String[]{ name },null,null,null);
        List<Integer> list = new ArrayList<Integer>();
        while (cursor.moveToNext()){
            list.add(cursor.getInt(cursor.getColumnIndex(MySQLiteOpenHelper.COLUMN_ID)));
        }
        cursor.close();
        return list.size() != 0;
    }

    public boolean insertData() {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        long id = mSQLiteDatabase.insert(MySQLiteOpenHelper.TABLE_MY_SDK, null, values);
        return id != -1;
    }

    public boolean updateData(int id, String name){
        if (Utils.isNullOrNil(name)) {
            return false;
        }
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mSQLiteOpenHelper.COLUMN_NAME, name);
        int result = mSQLiteDatabase.update(mSQLiteOpenHelper.TABLE_MY_SDK, values,mSQLiteOpenHelper.COLUMN_ID + " = ?", new String[]{ id + "" });
        if(result == 1) {
//            mContext.getContentResolver().notifyChange(BookWordContentProvider.PERSON_CONTENT_URI, null);
            return true;
        }
        return false;
    }

    public boolean deleteData(int id) {
        mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        int result = mSQLiteDatabase.delete(mSQLiteOpenHelper.TABLE_MY_SDK, mSQLiteOpenHelper.COLUMN_ID + " = ?", new String[]{ id + "" });
        return result != 0;
    }

    public void insertDataWithTransaction() {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        mSQLiteDatabase.beginTransaction();
        insertManyData();
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    public void insertManyData(){

    }
}