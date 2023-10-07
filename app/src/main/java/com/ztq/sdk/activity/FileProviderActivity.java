package com.ztq.sdk.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;
import android.widget.Toast;

import com.ztq.sdk.R;
import com.ztq.sdk.log.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileProviderActivity extends BaseActivity {
    private static final String TAG = "noahedu.FileProviderActivity";
    private ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_provider);

        mIv = findViewById(R.id.iv);

        File imagePath = new File(getFilesDir(), "images");
        File photoFile = new File(imagePath, "test.png");
        Log.v(TAG, "photoFile = " + photoFile);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            uri = FileProvider.getUriForFile(this, "com.ztq.sdk.fileProvider", photoFile);
            uri = Uri.parse("content://com.ztq.sdk.fileProvider/images/test.png");  // com.ztq.sdk.fileProvider是authority，images是file_paths中的name，test.png是文件简单名
        } else {
            uri = Uri.fromFile(photoFile);
        }
//        uri = Uri.fromFile(photoFile);
        Log.v(TAG, "uri = " + uri);

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String fileName = cursor.getString(cursor.getColumnIndex("_display_name"));
                long size = cursor.getLong(cursor.getColumnIndex("_size"));
                android.util.Log.v(TAG,"name: " + fileName + ", size: " + size);
                Toast.makeText(this, "name: " + fileName + "  size: " + size, Toast.LENGTH_SHORT).show();
            }
            cursor.close();
        } else {
            Toast.makeText(this, "cursor-result: 为空啊", Toast.LENGTH_SHORT).show();
        }
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            mIv.setImageBitmap(BitmapFactory.decodeStream(inputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        grantUriPermission("com.android.hanzidictation", uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        grantUriPermission("com.android.gallery3d", uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(uri, "image/*");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
//        startActivity(intent);
    }
}