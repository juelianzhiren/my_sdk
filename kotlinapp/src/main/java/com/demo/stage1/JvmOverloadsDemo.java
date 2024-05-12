package com.demo.stage1;

public class JvmOverloadsDemo {

    public static void main(String[] args) {
        // Java端
//        JvmOverloadsDemoKt.show("张三"); // Java无法享用 KT的默认参数

        JvmOverloadsDemoKt.toast("张三"); // 相当于 Java 享用 KT的默认参数
    }
}