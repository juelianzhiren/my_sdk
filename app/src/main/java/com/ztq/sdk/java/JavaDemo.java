package com.ztq.sdk.java;

/**
 * 运行在Java上个的demo例子
 */
public class JavaDemo {
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.class.path"));          //AppClassLoader;
        System.out.println(System.getProperty("java.ext.dirs"));              //ExtClassLoader;
        System.out.println(System.getProperty("sun.boot.class.path"));   //BootstrapClassLoader;
    }
}