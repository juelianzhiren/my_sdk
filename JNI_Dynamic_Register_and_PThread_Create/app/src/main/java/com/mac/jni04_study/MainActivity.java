package com.mac.jni04_study;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mac.jni04_study.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "JNISTUDY";

    static {
        // Java 的构造函数，构建对象的时候
        System.loadLibrary("jni04_study"); // JNI_OnLoad
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String str = stringFromJNI();
        Log.v(TAG, "str = " + str);
        staticRegister();

        // new Student();

        // 静态注册：
        // new Student.方法
        // new Student.方法
        // new Student.方法
        // new Student.方法
        // new Student.方法
        // new Student.方法

        // 动态注册：
        // s = new Student(); 所以的初始化已经做了
        // s.方法
        // s.方法
        // s.方法
        // s.方法
        // s.方法
    }

    /**
     * TODO 下面时native区域
     */
    // 第一部分 动态注册 区域 =====================================================
    public native String stringFromJNI(); // 默认生成的，其实也是静态注册方式

    public native void staticRegister(); // 静态注册 - 偷懒

    public native void dynamicJavaMethod01(); // 动态注册1 ()V

    public native int dynamicJavaMethod02(String valueStr); // 动态注册2  (Ljava/lang/String;)I

    // 第二部分 JNI线程 区域 =====================================================
    public native void naitveThread(); // Java层 调用 Native层 的函数，完成JNI线程

    public native void closeThread(); // 释放全局引用

    // 第三部分 纠结纠结细节 区域 ==================================================
    public native void nativeFun1(); // env 1000H

    public native void nativeFun2(); // env 1000H

    public static native void staticFun3(); // env 1000H

    public static native void staticFun4(); // env 2000H  Java的子线程

    /**
     * TODO 下面时点击事件区域
     */
    // 动态注册 的 点击事件1
    public void dynamic01(View view) {
        dynamicJavaMethod01();
    }

    // 动态注册 的 点击事件2
    public void dynamic02(View view) {
        int result = dynamicJavaMethod02("神照功");
        Toast.makeText(this, "result:" + result, Toast.LENGTH_SHORT).show();
    }

    // 第二部分 JNI线程 的 点击事件 =====================================================
    public void nativeCallJava(View view) {
        naitveThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeThread(); // 为了释放，全局成员
    }

    /**
     * TODO 下面是 被native代码调用的 Java方法
     * 第二部分 JNI线程
     */
    public void updateActivityUI() {
        if (Looper.getMainLooper() == Looper.myLooper()) { // TODO C++ 用主线程调用到此函数 ---->  主线程
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("UI")
                    .setMessage("updateActivityUI Activity UI ...")
                    .setPositiveButton("老夫知道了", null)
                    .show();
        } else {  // TODO  C++ 用异步线程调用到此函数 ---->  异步线程
            Log.d("Derry", "updateActivityUI 所属于子线程，只能打印日志了..");

            runOnUiThread(new Runnable() { // 哪怕是异步线程  UI操作 正常下去 runOnUiThread
                @Override
                public void run() {

                    // 可以在子线程里面 操作UI
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("updateActivityUI")
                            .setMessage("所属于子线程，只能打印日志了..")
                            .setPositiveButton("老夫知道了", null)
                            .show();
                }
            });
        }
    }

    // 第三部分 纠结纠结细节 区域 ==================================================
    // 纠结纠结细节
    public void clickMethod4(View view) {
        nativeFun1(); // main线程调用的  非静态
        nativeFun2(); // main线程调用的  非静态
        staticFun3(); // main线程调用的  静态

        // 第四个  new Thread 调用  ThreadClass == clasz 当前函数clazz地址
        new Thread() {
            @Override
            public void run() {
                super.run();
                staticFun4(); // Java的子线程调用  静态
            }
        }.start();

        new Thread() {
            @Override
            public void run() {
                super.run();
                staticFun4(); // Java的子线程调用  静态
            }
        }.start();
    }

    // 跳转到另外一个Activity
    public void clickMethod5(View view) {
        startActivity(new Intent(this, MainActivity2.class));
    }
}