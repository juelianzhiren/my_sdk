package com.demo

// TODO 高阶函数第五版  Derry KT精华

fun main() {
    // 以前的做法(匿名函数体)
    showAction {
        "我的值是:$it"
    }

    // 一般源码的做法
    showAction(::lambdaImpl)

    // :: 把这个函数变成函数引用，就可以传递赋值给变量了

    val r1 : Function1<Int, String> = ::lambdaImpl
    val r2 : (Int) -> String = r1
    val r3 : Int.() -> String = r2 // Int.() == (Int)   Int.() Int.属于来源，会自动把来源作为第一个参数

    // Int.() -> String 接收类型   (Int) -> String

    // Int.(String) -> Unit方法是两个参数的？  == (Int, String) -> Unit

    showAction(r3)
}

fun lambdaImpl(value: Int) = "我的值是:$value"

fun showAction(action: (Int) -> String) {
    println(action(88))
}