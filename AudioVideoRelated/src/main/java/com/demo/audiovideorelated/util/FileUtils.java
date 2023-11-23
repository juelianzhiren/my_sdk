package com.demo.audiovideorelated.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class FileUtils {
    /**
     * 扫描sd卡，进行更新
     *
     * @param context
     * @param file
     */
    public static void notifySystemToScan(Context context, File file) {
        if (context == null || file == null || !file.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }
}