package com.demo.stage1;

import com.derry.s7.MyObject;

public class JvmStaticDemo {
    public static void main(String[] args) {
        // Java 端
        System.out.println(MyObject.TARGET);

        MyObject.showAction("Kevin");

//        System.out.println(MyObject.Companion.getTARGET());
//
//        MyObject.Companion.com.demo.showAction("Kevin");
    }
}