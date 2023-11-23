package com.demo.audiovideorelated.util;

import android.app.Application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author: zhengjf
 * Date: 2020/10/26 14:16
 * Description: 获取Application实例
 */
public class ApplicationGetter {
    private static Application sInstance;

    public static Application get() {
        if (sInstance != null) {
            return sInstance;
        }

        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Method method1 = activityThreadClass.getMethod("currentActivityThread");
            method1.setAccessible(true);
            Object activityThread = method1.invoke(null);
            Method method2 = activityThread.getClass().getMethod("currentApplication");
            method2.setAccessible(true);
            sInstance = (Application) method2.invoke(activityThread);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return sInstance;
    }
}