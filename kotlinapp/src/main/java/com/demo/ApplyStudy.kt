package com.demo

import java.io.File

fun main() {
    // TODO 官方的：
    // Region+ ===
    // 错误的用法
    val r2 : File = File("D:\\a.txt").apply {
        readLines()
    }.apply {
        setWritable(true)
    }.apply {
        setReadable(true)
    }.apply {
        // ...
    }

    // 正确用法
    File("D:\\a.txt").apply {
        readLines()
    }.apply {
        setWritable(true)
    }.apply {
        setReadable(true)
    }.apply {
        // ...
    }
    // endRegion

    val r : String = "Derry".apply {
        ""
        true
        9
        println(this)
    }.apply {

    }.apply {

    }.apply {

    }


    // TODO 我们自己写的人
    "Derry".myApply {

    }.myApply {

    }.myApply {

    }

    123.myApply {

    }

    true.myApply {

    }.myApply {

    }
}

// 只要是高阶函数，必须用inline修饰，为什么，因为内部会对lambda做优化
// I.myApply 万能类型.myApply 所有类型都可以调用
// lambda : I.()  对I泛型进行了匿名函数扩展  好处 lambda持有this == I == "Derry"
// : I 为了链式调用
//  lambda() 调用执行
//
private inline fun <I> I.myApply(lambda : I.() -> Unit) : I {
    lambda()
    return this
}