package com.ztq.sdk.utils;

import android.text.TextUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 */
public class ReflectUtils {
    public static Object invoke(Object obj, String methodName, Object[] args) {
        if (obj == null || TextUtils.isEmpty(methodName)) {
            return null;
        }
        args = args == null ? new Object[0] : args;
        Class[] paramTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }
        try {
            Method method = obj.getClass().getMethod(methodName, paramTypes);
            boolean isAccessible = method.isAccessible();
            if (!isAccessible) {
                method.setAccessible(true);
            }
            Object result = method.invoke(obj, args);
            if (!isAccessible) {
                method.setAccessible(false);
            }
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeStatic(Class cls, String methodName, Object[] args) {
        if (cls == null || TextUtils.isEmpty(methodName)) {
            return null;
        }
        args = args == null ? new Object[0] : args;
        Class[] paramTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }
        try {
            Method method = cls.getMethod(methodName, paramTypes);
            boolean isAccessible = method.isAccessible();
            if (!isAccessible) {
                method.setAccessible(true);
            }
            Object result = method.invoke(null, args);
            if (!isAccessible) {
                method.setAccessible(false);
            }
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getStaticField(Class cls, String fieldName) {
        if (cls == null || TextUtils.isEmpty(fieldName)) {
            return null;
        }
        try {
            Field field = cls.getField(fieldName);
            boolean isAccessible = field.isAccessible();
            if (!isAccessible) {
                field.setAccessible(true);
            }
            Object result = field.get(null);
            if (!isAccessible) {
                field.setAccessible(false);
            }
            return result;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取cls类中所有的方法（包括自身声明方法，也包括从子类继承的方法）
     * @param cls
     * @return
     */
    public static List<Method> getAllMethods(Class cls) {
        if (cls == null) {
            return null;
        }
        Class currentCls = cls;
        List<Method> list = new ArrayList<>();
        while(currentCls != null) {
            Method[] declaredMethods = currentCls.getDeclaredMethods();
            list.addAll(Arrays.asList(declaredMethods));
            currentCls = currentCls.getSuperclass();
        }
        return list;
    }
}