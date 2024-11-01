package com.demo.stage1.single_instance;

// 3.懒汉式的实现  Java版本 安全
public class SingletonDemo3 {
    private static SingletonDemo3 instance;

    private SingletonDemo3() {}

    public static synchronized SingletonDemo3 getInstance() {
        if (instance == null) {
            instance = new SingletonDemo3();
        }
        return instance;
    }

    public void show() {
        System.out.println("com.demo.show");
    }

    public static void main(String[] args) {
        SingletonDemo3.getInstance().show();
    }
}