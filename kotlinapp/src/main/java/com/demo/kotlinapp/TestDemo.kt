package com.demo.kotlinapp

import java.lang.Exception
import java.lang.Integer.parseInt

class TestDemo {
    fun cases(obj : Any) {
        when (obj) {
            1 -> print("第一项")
            "hello" -> print("这是字符串hello")
            is Long -> print("这是一个Long类型数据")
            !is String -> print("这不是String类型的数据")
            else -> print("else类似于Java中的default")
        }
    }

    fun switch(x: Any) {
        val s = "123"
        when(x) {
            -1, 0 -> print("x == -1 or x == 0")
            1 -> print("x == 1")
            2 -> print("x == 2")
            8 -> print("x is 8")
            parseInt(s) -> println("x is 123")
            else -> {
                print("x is neither 1 or 2")
            }
        }
    }

    fun switch2(x: Any) {
//        val x = 1
        val validNumbers = arrayOf(1, 2, 3)
        when (x) {
            in 1..10 -> print("x is in the range")
            in validNumbers -> print("x is valid")
            !in 10..20 -> print("x is outside the range")
            else -> print("none of the above")
        }
    }
}

fun main(args : Array<String>) {
    var demo = TestDemo();
    demo.cases(2L)
    println()
    demo.switch("a")
    println()
    demo.switch2(14)

    println()
    for (arg in args) {
        println(arg)
    }
    println()
    for(i in args.indices) {
        println(args[i])
    }
    println(args.indices)
    for((index, value) in args.withIndex()) {
        println("the element at $index is $value")
    }

    println(sum(10, 1))
    println(max(10, 1))

    val sum = fun(a:Int, b:Int) = a+b
    println(sum)

    println(sum(1,3))

    println("------------")
    val sumf = fun(a:Int, b:Int) = {a + b}
    println(sumf)
    println(sumf(1,1))
    println(sumf(1,1).invoke())

    println("-----------------------------")
    breakDemo_1()
    println("------------------")
    continueDemo()
    println("------------------")
    returnDemo_1()
    println("------------------")
    returnDemo_2()
    println("------------------")
    returnDemo_3()
    println("------------------")
    returnDemo_4()
    println("------------------")
    breakDemo_2()
    println("------------------")
    breakDemo_3()
    println("------------------")
    breakDemo_4()

    println("------------------")
    val ex:Nothing = throw Exception("YYYYYYYY")
//    println(ex)
}

fun sum(a: Int, b: Int):Int {
    return a + b
}

fun max(a: Int, b: Int):Int {
    if (a > b) {
        return a
    } else {
        return b
    }
}

fun breakDemo_1(){
    for(i in 1..10) {
        println(i)
        if (i % 2 == 0) {
            break;
        }
    }
}

fun continueDemo() {
    for (i in 1..10) {
        if (i % 2 == 0) {
            continue
        }
        println(i)
    }
}

fun returnDemo_1() {
    println("	START	" + ::returnDemo_1.name)
    val intArray = intArrayOf(1, 2, 3, 4, 5)
    intArray.forEach {
        if (it == 3) {
            return
        }
        println(it)
    }
    println("	END	" + ::returnDemo_1.name)
}

fun returnDemo_2() {
    println("	START	" + ::returnDemo_2.name)
    val intArray = intArrayOf(1, 2, 3, 4, 5)
    intArray.forEach(fun(a: Int) {
        if (a == 3) {
            return
        }
        println(a)
    })
    println("	END	" + ::returnDemo_2.name)
}

fun returnDemo_3() {
    println("   START  " + ::returnDemo_3.name)
    val intArray = intArrayOf(1, 2, 3, 4, 5)
    intArray.forEach here@{
        if (it == 3) {
            return@here
        }
        println(it)
    }
    println("  END  " + ::returnDemo_3.name)
}

fun returnDemo_4() {
    println("   START  " + ::returnDemo_4.name)
    val intArray = intArrayOf(1, 2, 3, 4, 5)
    intArray.forEach {
        if (it == 3) {
            return@forEach
        }
        println(it)
    }
    println("  END  " + ::returnDemo_4.name)
}

fun breakDemo_2() {
    println("---------------		breakDemo_2	---------------")
    for (outer in 1..5) {
        println("outer=" + outer)
        for (inner in 1..10) {
            println("inner=" + inner)
            if (inner % 2 == 0) {
                break
            }
        }
    }
}

fun breakDemo_3() {
    println("---------------		breakDemo_3	---------------")
    outer@ for (outer in 1..5) {
        for (inner in 1..10) {
            println("inner=" + inner)
            println("outer=" + outer)
            if (inner % 2 == 0) {
                break@outer
            }
        }
    }
}

fun breakDemo_4() {
    println("---------------		breakDemo_4	---------------")
    for (outer in 1..5) {
        inner@ for (inner in 1..10) {
            println("inner=" + inner)
            println("outer=" + outer)
            if (inner % 2 == 0) {
                break@inner
            }
        }
    }
}