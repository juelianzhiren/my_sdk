package com.ztq.sdk.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by ztq on 2019/11/14.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private final String TAG = "noahedu.MySQLiteOpenHelper";
    private Context mContext;
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NAME = "name";
    private static String DATABASE_NAME = "my.db";
    public static final String TABLE_MY_SDK = "my_sdk";
    public final static int DATABASE_VERSION = 8;          // 版本号

    public MySQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_MY_SDK + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY, "
                    + COLUMN_NAME + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
    }
}