package com.demo.stage1.single_instance;

// 1.饿汉式的实现  Java版本
public class SingletonDemo {

    private static SingletonDemo instance =  new SingletonDemo();

    private SingletonDemo() {}

    public static SingletonDemo getInstance() {
        return instance;
    }
}
