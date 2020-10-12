package com.ztq.sdk.utils;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.BidiFormatter;
import android.text.TextDirectionHeuristics;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ztq.sdk.R;
import com.ztq.sdk.adapter.OneDataSourceAdapter;
import com.ztq.sdk.helper.MyHandlerThread;
import com.ztq.sdk.log.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 工具类
 */
public class Utils {
    private static final String TAG = "noahedu.Utils";
    private static Toast mToast;
    public static final String LEFT_BRACKET = "[";
    public static final String RIGHT_BRACKET = "]";
    public static final String COLON = ":";
    public static final String DOT = ".";
    public static final String DASH = "-";
    public static final int MINUTE_CONTAINS_SECONDS_OR_HOUR_CONTATNS_MINUTE = 60;

    public static String getNullOrNil(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    public static int getInt(String str) {
        int result = 0;
        try {
            result = Integer.parseInt(str);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取到形如[02:30.10]的时间数，单位为毫秒
     * @param timeStr
     * @return
     */
    public static int getTime(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) {
            return -1;
        }
        timeStr = timeStr.replace(LEFT_BRACKET, "").replace(RIGHT_BRACKET, "");
        int time = 0;
        String[] arr = timeStr.split(COLON);
        if (arr != null) {
            for(int i = 0; i < arr.length; i++) {
                String str = arr[i];
                if (!TextUtils.isEmpty(str)) {
                    time += (int)(getFloat(str) * Math.pow(MINUTE_CONTAINS_SECONDS_OR_HOUR_CONTATNS_MINUTE, arr.length - 1 - i) * 1000);
                }
            }
        }
        return time;
    }


    public static boolean isNullOrNil(String str) {
        if (str == null || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static float getFloat(String str) {
        float result = 0.0f;
        try {
            result = Float.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static double getDouble(String str) {
        double result = 0.0;
        try {
            result = Double.valueOf(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void writeMsgToFile(final String path, final String str) {
        MyHandlerThread.postToWorker1(new Runnable() {
            @Override
            public void run() {
                if (isNullOrNil(path) || isNullOrNil(str)) {
                    return;
                }
                File file = new File(path);
                if (file.isDirectory()) {
                    return;
                }
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    FileWriter fw = new FileWriter(file, true);
                    fw.write(str);
                    fw.flush();
                    fw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void writeExceptionToFile(final String path, final Throwable throwable){
        MyHandlerThread.postToWorker1(new Runnable() {
            @Override
            public void run() {
                if (isNullOrNil(path) || throwable == null) {
                    return;
                }
                File file = new File(path);
                if (file.isDirectory()) {
                    return;
                }
                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    PrintWriter pw = new PrintWriter(new FileWriter(file, true));
                    throwable.printStackTrace(pw);
                    pw.flush();
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void showToast(Context context, final String msg) {
        if (context == null || isNullOrNil(msg)) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.toast, null);
        final TextView text = (TextView) view.findViewById(R.id.toast_msg_tv);
        text.setText(msg);
        if(mToast == null) {
            mToast = new Toast(context);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, -100);
            mToast.setView(view);
        } else {
            mToast.cancel();
            MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    text.setText(msg);
                    mToast.setView(view);
                    mToast.show();
                }
            }, 100);
        }
        mToast.show();
    }

    public static void showToast(Context context, final int resId) {
        if (context == null || resId <= 0) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.toast, null);
        final TextView text = (TextView) view.findViewById(R.id.toast_msg_tv);
        text.setText(resId);
        if(mToast == null) {
            mToast = new Toast(context);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, -100);
            mToast.setView(view);
        } else {
            mToast.cancel();
            MyHandlerThread.postToMainThreadDelayed(new Runnable() {
                @Override
                public void run() {
                    text.setText(resId);
                    mToast.setView(view);
                    mToast.show();
                }
            }, 100);
        }
        mToast.show();
    }

    public static String getFormatTime(Date date, String format) {
        if (date == null || isNullOrNil(format)) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 将100000ms转化成00:10的形式
     * @param millisecs
     * @return
     */
    public static String getFormatTime(long millisecs) {
        if (millisecs <= 0) {
            return "00:00";
        }
        int seconds = (int)millisecs / 1000;
        int minutes = 0;
        int hours = 0;
        if (seconds < 60) {
            return getCompleteStr(minutes) + ":" + getCompleteStr(seconds);
        } else if (seconds < 3600) {
            minutes = seconds / 60;
            seconds = seconds % 60;
            return getCompleteStr(minutes) + ":" + getCompleteStr(seconds);
        } else {
            hours = seconds / 3600;
            minutes = (seconds - hours * 3600) / 60;
            seconds = (seconds - hours * 3600) % 60;
            return getCompleteStr(hours) + ":" + getCompleteStr(minutes) + ":" + getCompleteStr(seconds);
        }
    }

    public static String getCompleteStr(int secondsOrMinutes) {
        String result = "";
        if (secondsOrMinutes < 10) {
            result = "0" + secondsOrMinutes;
        } else if (secondsOrMinutes < 60) {
            result = secondsOrMinutes + "";
        }
        return result;
    }

    /**
     * 在dir的文件下新建.nomedia文件，用来屏蔽媒体软件扫描
     * @param dirStr
     */
    public static void createNomediaFile(String dirStr) {
        if (isNullOrNil(dirStr)) {
            return;
        }
        File dirFile = new File(dirStr);
        if (!dirFile.isDirectory()) {
            return;
        }
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        File file = new File(dirStr, ".nomedia");
        if (!file.exists()) {
            try {
                file.createNewFile();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 压缩单个文件
     * @param srcFile
     * @param targetFile
     */
    public static boolean zipFile(File srcFile, File targetFile){
        if(srcFile == null || targetFile == null || !srcFile.exists()){
            return false;
        }
        ZipOutputStream out = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(targetFile));
            byte[] buf = new byte[1024];
            FileInputStream in = null;
            int len;
            in = new FileInputStream(srcFile);
            out.putNextEntry(new ZipEntry(srcFile.getName()));
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            out.close();
            in.close();
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取当前时区与0时区（格林威治时间）的时间偏差（以秒为单位），例如，当前时区为东8区，那结果为8*60*60即28800秒
     * @return
     */
    public static long getTimeZoneOffset(){
        Date date = new Date();
        int offset = date.getTimezoneOffset();   // 这个方法得到的是分钟数
        return -offset * 60;
    }

    /**
     * 将路径为videoPath的视频缩略图转化为对应的图片文件
     *
     * @param videoPath
     * @param width
     * @param height
     * @param kind
     *            参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。 其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @param filePath
     * @return boolean
     */
    public static boolean transformVideoThumbnail2File(String videoPath, int width, int height, int kind, String filePath) {
        if(isNullOrNil(videoPath) || width <= 0 || height <= 0 || isNullOrNil(filePath) || !(new File(videoPath).exists())){
            return false;
        }
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height);

        if (bitmap == null) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fout);
            fout.flush();
            fout.close();
            bitmap.recycle();
            bitmap = null;
            return true;
        } catch (IOException e) {
            bitmap.recycle();
            return false;
        }
    }

    /**
     * 获取本地图片的bitmap
     * @param path
     * @return
     */
    public static Bitmap getLoacalBitmap(String path) {
        if(isNullOrNil(path)){
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setDrawableLeft(Context context, TextView tv, int resId) {
        if (context == null || tv == null || resId <= 0) {
            return;
        }
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable,null,null,null);
    }

    public static void setDrawableRight(Context context, TextView tv, int resId) {
        if (context == null || tv == null || resId <= 0) {
            return;
        }
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(null,null,drawable,null);
    }

    public static void setDrawableTop(Context context, TextView tv, int resId) {
        if (context == null || tv == null || resId <= 0) {
            return;
        }
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(null,drawable,null,null);
    }

    public static void setDrawableBottom(Context context, TextView tv, int resId) {
        if (context == null || tv == null || resId <= 0) {
            return;
        }
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(null,null,null,drawable);
    }

    /**
     * 获取进程名称
     * @param context
     * @return
     */
    public static String getProcessName(Context context) {
        if (context == null) {
            return null;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    /**
     * 使view控件获取焦点
     * @param view
     */
    public static void requestFocus(View view){
        if(view == null){
            return;
        }
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.requestFocusFromTouch();
    }

    /**
     * scrollview嵌套listView，计算listView高高度
     * */

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public static float getDensity(Context context) {
        if (context == null) {
            return 0f;
        }
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.density;
    }

    public static int dp2px(Context context, int dpValue) {
        if (context == null) {
            return -1;
        }
        return (int)(getDensity(context) * dpValue + 0.5);
    }

    public static int px2dp(Context context,int pxValue) {
        if (context == null) {
            return -1;
        }
        return (int)((pxValue - 0.5) / getDensity(context));
    }

    public static String getVersionName(Context context) {
        if (context == null) {
            return null;
        }
        String versionName = "";
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 检查是否为邮箱
     */
    public static boolean checkEmail(String str) {
        if (isNullOrNil(str)) {
            return false;
        }
        Pattern p = Pattern.compile("([a-zA-Z0-9]+[_|\\-|\\.]?)*[a-zA-Z0-9]+@" + "([a-zA-Z0-9]+[_|\\-|\\.]?)" + "*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 通过反射获取通知的开关状态
     * @param context
     * @return
     */
    public static boolean isNotificationEnabled(Context context){
        if (context == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            Class appOpsClass = null; /* Context.APP_OPS_MANAGER */
            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (int)opPostNotificationValue.get(Integer.class);
                return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    // 显示某个日期的下一个月的年月信息
    public static String getNextMonthAndYear(Date date){
        if (date == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        Date date0 =  calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String nextMonthAndYear = sdf.format(date0);
        return nextMonthAndYear;
    }

    public static Bitmap decodeBitmapFromUri(ContentResolver res, Uri uri, int reqWidth, int reqHeight) throws IOException {
        if (res == null || uri == null || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(res.openInputStream(uri), null, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(res.openInputStream(uri), null, options);
    }

    private static int calculateInSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        if (options == null || reqWidth <= 0 || reqHeight <= 0){
            return -1;
        }
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        /**如果算出来的inSampleSize == 0，则直接将inSampleSize置为1，表示不用压缩*/
        if(inSampleSize == 0){
            inSampleSize = 1;
        }

        Log.v(TAG, "height = " + options.outHeight + "; width = " + options.outWidth + ";inSampleSize = " + inSampleSize);

        return inSampleSize;
    }

    public static Bitmap decodeBitmapFromFile(String path, int reqWidth, int reqHeight) {
        if (isNullOrNil(path) || reqHeight <= 0 || reqWidth <= 0) {
            return null;
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 创建图片文件
     *
     * @param res
     * @param uri  原始图片uri
     * @param outputPath 转换后图片地址
     * @param reqWidth
     * @param reqHeight
     */
    public static Bitmap createBitmapFile(ContentResolver res, Uri uri, String outputPath, int reqWidth, int reqHeight) {
        if (res == null || uri == null || Utils.isNullOrNil(outputPath) || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }
        Bitmap bm = null;
        try {
            bm = decodeBitmapFromUri(res, uri, reqWidth, reqHeight);
            File file = new File(outputPath);
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 77, fout);
            fout.flush();
            fout.close();
            Log.e(TAG, "create thumb file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * 选择变换
     *
     * @param bitmap 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int alpha) {
        if (bitmap == null) {
            return null;
        }
        if(alpha % 360 == 0){
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
        return bitmap;
    }

    /**
     * 创建图片文件
     *
     * @param inputPath  原始图片地址
     * @param outputPath 转换后图片地址
     */
    public static Bitmap createBitmapFile(String inputPath, String outputPath, int reqWidth, int reqHeight, int orientation) {
        if (isNullOrNil(inputPath) || isNullOrNil(outputPath) || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }

        Log.i(TAG, "createBitmapFile inputPath:" + inputPath);
        Bitmap bm = null;
        try {
            bm = decodeBitmapFromFile(inputPath, reqWidth, reqHeight);
            bm = rotateBitmap(bm, orientation);
            File file = new File(outputPath);
            file.createNewFile();
            FileOutputStream fout = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 77, fout);     //77%
            fout.flush();
            fout.close();
            Log.i(TAG, "createBitmapFile outputPath:" + outputPath + ", output file size = " + file.length());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e(TAG, "获取图片失败");
        }
        return bm;
    }

    /**
     * 检查是否存在SD卡
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 创建目录
     *
     * @param context
     * @param dirName
     *            文件夹名称
     * @return
     */
    public static File createFileDir(Context context, String dirName) {
        if (context == null || isNullOrNil(dirName)) {
            return null;
        }
        String filePath;
        // 如SD卡已存在，则存储；反之存在data目录下
        if (hasSdcard()) {
            // SD卡路径
            filePath = Environment.getExternalStorageDirectory() + File.separator + dirName;
        } else {
            filePath = context.getCacheDir().getPath() + File.separator + dirName;
        }
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            boolean isCreate = destDir.mkdirs();
        }
        return destDir;
    }

    /**
     * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        if (file == null) {
            return -1;
        }
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    int num = subFiles.length;
                    for (int i = 0; i < num; i++) {
                        size += getFileSize(subFiles[i]);
                    }
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

    /**
     * 动态刷新listview的单条数据,
     */
    public static void updateSingleItemInListview(ListView listView, OneDataSourceAdapter adapter, int position) {
        if (listView == null || adapter == null || position < 0) {
            return;
        }
        int visibleFirstPosi = listView.getFirstVisiblePosition();
        int visibleLastPosi = listView.getLastVisiblePosition();

        if (position >= visibleFirstPosi && position < visibleLastPosi) {
            View view = listView.getChildAt(position - visibleFirstPosi);
            adapter.getView(position, view, listView);
        }
    }

    // 每个拼音单元长度以7个字符长度为标准,拼音居中,末尾优先
    public static String formatCenterUnit(String unit) {
        String result = unit;
        switch(unit.length()) {
            case 1:
                result = "   " + result + "   ";
                break;
            case 2:
                result = "  " + result + "   ";
                break;
            case 3:
                result = "  " + result + "  ";
                break;
            case 4:
                result = " " + result + "  ";
                break;
            case 5:
                result = " " + result + " ";
                break;
            case 6:
                result = result + " ";
                break;
        }
        return result;
    }

    public static List<String> getPinyinString(String hanzi) {
        if (hanzi != null && hanzi.length() > 0) {
            List<String> pinyinList = new ArrayList<>();
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
            for (int index = 0; index < hanzi.length(); index++) {
                char c = hanzi.charAt(index);
                try {
                    String[] pinyinUnit = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (pinyinUnit == null) {
                        pinyinList.add(index, "null");  // 非汉字字符，如标点符号
                        continue;
                    } else {
                        Log.v(TAG, "char, pinyin " + index + ": pinyinUnit[0] = " + pinyinUnit[0]);
                        pinyinList.add(index, formatCenterUnit(pinyinUnit[0].substring(0, pinyinUnit[0].length() - 1)) + pinyinUnit[0].charAt(pinyinUnit[0].length() - 1));  // 带音调且长度固定为7个字符长度,,拼音居中,末尾优先
                        Log.e(TAG, pinyinList.get(index));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                    badHanyuPinyinOutputFormatCombination.printStackTrace();
                }
            }
            return pinyinList;
        } else {
            return null;
        }
    }

    public static List<String> getFormatHanzi(String hanzi) {
        if (hanzi != null && hanzi.length() > 0) {
            char[] c = hanzi.toCharArray();
            List<String> hanziList = new ArrayList<>();
            for (int index = 0; index < c.length; index++) {
               hanziList.add(c[index] + "");
                Log.v(TAG, "char " + index + "; " + c[index]);
            }
            return hanziList;
        } else {
            return null;
        }
    }

    public static Bitmap getBitmapFromDrawableRes(Context context, int resId) {
        if (context == null || resId <= 0) {
            return  null;
        }
        Resources res = context.getResources();
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId);
        return bitmap;
    }

    public static Bitmap changeBitmap(Bitmap bitmap, float scale) {
        if (bitmap == null || scale <= 0) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
    }

    /**
     * 方法描述：判断某一应用是否正在运行(有个缺点，除了桌面外，其它系统的app信息获取不到)
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        if (context == null || isNullOrNil(packageName)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        Log.v(TAG, "list size = " + list.size());
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            Log.v(TAG, "list packagename = " + info.baseActivity.getPackageName());
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活(有个缺点，除了桌面外，其它系统的app信息获取不到)
     * Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param uid 已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, int uid) {
        if (context == null || uid < 0) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos){
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }

    //获取已安装应用的 uid，-1 表示未安装此应用或程序异常
    public static int getPackageUid(Context context, String packageName) {
        if (context == null || isNullOrNil(packageName)) {
            return -1;
        }
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    public static boolean isAppOrProcessRunnings(Context context, String packageName) {
        int uid = getPackageUid(context, packageName);
        if(uid > 0){
            boolean isAppRunning = isAppRunning(context, packageName);
            boolean isProcessRunning = isProcessRunning(context, uid);
            Log.v(TAG, "isAppRunning = " + isAppRunning + "; isProcessRunning = " + isProcessRunning);
            return isAppRunning | isProcessRunning;
        }else{
            return false;
        }
    }

    /**
     * 判断mediaPlayer是否在播放
     * @param player
     * @return
     */
    public static boolean isPlaying(MediaPlayer player) {
        boolean flag = false;
        if (player != null) {
            try {
                flag = player.isPlaying();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag;
    }

    /**
     * 得到图片字节流 数组大小
     * */
    public static byte[] inputStream2ByteArray(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.flush();
            return outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int[] getScreenSize(Context context) {
        if (context == null) {
            return null;
        }
        int arr[] = new int[2];
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        arr[0] = dm.widthPixels;
        arr[1] = dm.heightPixels;
        return arr;
    }

    /**
     * 方法：检查表中某列是否存在
     *
     * @param db
     * @param tableName 表名
     * @param columnName 列名
     * @return
     */
    public static boolean checkColumnExists(SQLiteDatabase db, String tableName, String columnName) {
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?", new String[] { tableName, "%" + columnName + "%" });
            result = (null != cursor && cursor.moveToFirst());
        } catch (Exception e) {
            Log.e(TAG, "checkColumnExists exception: " + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }

    public interface NetTimeListener<T> {
        void onReceiveTime(long time, T t);
    }

    public static void getTimeFromNet(final NetTimeListener listener, final Object obj) {
        MyHandlerThread.postToWorker1(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL("http://quan.suning.com/getSysTime.do");
                    urlConnection = (HttpURLConnection) url.openConnection();//生成连接对象
                    urlConnection.setConnectTimeout(5000);
                    urlConnection.setRequestMethod("GET");
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertStreamToString(in);
                    final JSONObject json = new JSONObject(response);
                    if (json.has("sysTime2")) {
                        final String date = json.getString("sysTime2");
                        long time =  dateToTimestamp(date);
                        if (listener != null) {
                            listener.onReceiveTime(time, obj);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    long time = System.currentTimeMillis();
                    if (listener != null) {
                        listener.onReceiveTime(time, obj);
                    }
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });
    }

    public static final long dateToTimestamp(String time) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final Date date;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            return 0;
        }
        return date.getTime();
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 扫描sd卡，进行更新
     * @param context
     * @param file
     */
    public static void notifySystemToScan(Context context, File file) {
        if (context == null || file == null || !file.exists()) {
            return;
        }
//        Log.v(TAG, "filePth = " + file.getAbsolutePath());
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);
    }

    public static String getResuorceString(Context context, int stringId) {
        if (context == null || stringId <= 0) {
            return "";
        }
        return context.getResources().getString(stringId);
    }

    /**
     * 连接Get请求url，空格用%20替换
     */
    public static String joinGetUrl(String url, Map<String, String> param) {
        if (isNullOrNil(url)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        if(param != null && !param.isEmpty()) {
            for(Map.Entry<String, String> entry:param.entrySet()) {
                if(isFirst) {
                    builder.append("?");
                    isFirst = false;
                } else {
                    builder.append("&");
                }
                builder.append(entry.getKey() + "=" + entry.getValue());
            }
        }
        String paramStr = builder.toString();
        paramStr = paramStr.replaceAll(" ", "%20");//Volley框架Get请求，参数带空格会报错505
        return url + paramStr;
    }

    /**
     * 设置为原型图片
     * @param source
     * @return
     */
    public static Bitmap createCircleImage(Bitmap source) {
        if (source == null) {
            return null;
        }
        int length = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(length / 2, length / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height").get(object).toString());
            statusHeight = context.getApplicationContext().getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        if (context == null) {
            return -1;
        }
        int result = -1;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        if (context == null) {
            return -1;
        }
        int result = -1;
        int resourceId = 0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getTimeZone() {
        Calendar mDummyDate;
        mDummyDate = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        mDummyDate.setTimeZone(now.getTimeZone());
        mDummyDate.set(now.get(Calendar.YEAR), 11, 31, 13, 0, 0);
        return getTimeZoneText(now.getTimeZone(), true);
    }

    public static String getTimeZoneText(TimeZone tz, boolean includeName) {
        Date now = new Date();

        SimpleDateFormat gmtFormatter = new SimpleDateFormat("ZZZZ");
        gmtFormatter.setTimeZone(tz);
        String gmtString = gmtFormatter.format(now);
        BidiFormatter bidiFormatter = BidiFormatter.getInstance();
        Locale l = Locale.getDefault();
        boolean isRtl = TextUtils.getLayoutDirectionFromLocale(l) == View.LAYOUT_DIRECTION_RTL;
        gmtString = bidiFormatter.unicodeWrap(gmtString, isRtl ? TextDirectionHeuristics.RTL : TextDirectionHeuristics.LTR);
        if (!includeName) {
            return gmtString;
        }
        return gmtString;
    }

    /**
     * 将bitmap保存到本地localPath路径下
     * @param bitmap
     * @param localPath
     */
    public static void saveBitmapToLocalPath(Bitmap bitmap, String localPath) {
        if (bitmap == null || isNullOrNil(localPath)) {
            return;
        }
        File file = new File(localPath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromLocalPath(String localPath) {
        try {
            FileInputStream fis = new FileInputStream(localPath);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取私有成员变量的值
     * @param obj
     * @param filedName
     * @return
     */
    public static Object getPrivateFieldObject(Object obj, String filedName) {
        try {
            Field field = getFieldName(obj.getClass(), filedName);
            field.setAccessible(true);
            Object result = field.get(obj);
            field.setAccessible(false);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void invokeMethod(Object obj, String className, String methodName, Class[] parameterClsTypes, Object[] parmeterValues) {
        try {
            Class cls = Class.forName(className);
            Method method = cls.getMethod(methodName, parameterClsTypes);
            method.setAccessible(true);
            method.invoke(obj, parmeterValues);
            method.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getFieldName(Class<?> cls, String filedName) {
        List<Field> allFieldsList = getAllFieldsList(cls);
        if (allFieldsList != null) {
            for(int i = 0; i < allFieldsList.size(); i++) {
                Field field = allFieldsList.get(i);
                if (field != null) {
                    if (field.getName().equals(filedName)) {
                        return field;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 获取当前类和父类的所有属性
     * @param cls
     * @return
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                allFields.add(field);
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    public interface DownloadListener {
        void onStart(String url);
        void onSuccess(String url);
        void onFailure(String url);
        void onUpdate(String url, float progress);
    }
    private static int mProgress = 0;
    private static OkHttpClient mOkHttpClient;
    public static void downloadFile(final String remoteUrl, final String localPath, final DownloadListener listener) {
        if (isNullOrNil(remoteUrl) || isNullOrNil(localPath)) {
            return;
        }
        if (listener != null) {
            listener.onStart(remoteUrl);
        }
        final long startTime = System.currentTimeMillis();
        Log.v(TAG, "startTime = " + startTime);
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        Request request = new Request.Builder()
                .url(remoteUrl)
                .addHeader("Connection", "close")
                .build();
        mProgress = 0;
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e(TAG, "download failed, msg = " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                File file = new File(localPath);
                if (file.isDirectory()) {
                    Log.e(TAG, "wanted file, but directory");
                    if (listener != null) {
                        listener.onFailure(remoteUrl);
                    }
                    return;
                }
                if (!file.getParentFile().exists()) {
                    boolean flag = file.getParentFile().mkdirs();
                    if (!flag) {
                        Log.e(TAG, "create parent directory fail");
                        if (listener != null) {
                            listener.onFailure(remoteUrl);
                        }
                        return;
                    }
                }
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.v(TAG, "download progress: " + progress);
                        if (progress != mProgress) {
                            if (listener != null) {
                                listener.onUpdate(remoteUrl, progress);
                            }
                            mProgress = progress;
                        }
                    }
                    fos.flush();
                    if (listener != null) {
                        listener.onSuccess(remoteUrl);
                    }
                    Log.v(TAG, "download success, " + "totalTime = " + (System.currentTimeMillis() - startTime) + "ms.");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "download failed : " + e.getMessage());
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}