package com.demo.kotlinapp

class ThisDemo {
    val thisis = "THIS	IS"

    fun whatIsThis(): ThisDemo {
        println(this.thisis)    //引用变量
        this.howIsThis()//	引用成员函数
        return this    //	返回此类的引用
    }

    fun howIsThis() {
        println("HOW	IS	THIS	?")
    }
}

fun main() {
    var demo = ThisDemo()
    println(demo.whatIsThis())

    val sum = fun Int.(x:Int):Int=this+x
    sum
}