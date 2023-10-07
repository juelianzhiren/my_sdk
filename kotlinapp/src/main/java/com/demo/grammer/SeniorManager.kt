package com.demo.grammer

import java.io.File

interface Driver {
    fun drive()
}

interface Writer {
    fun write()
}

class CarDriver : Driver {
    override fun drive() {
        println("开车呢")
    }
}

class PPTWriter : Writer {
    override fun write() {
        println("做PPT呢")
    }
}

class Manager : Driver, Writer {
    override fun drive() {

    }

    override fun write() {

    }
}

class SeniorManager(val driver: Driver, val writer: Writer) : Driver by driver, Writer by writer

fun main() {
    val driver = CarDriver()
    val writer = PPTWriter()
    val seniorManager = SeniorManager(driver, writer)
    seniorManager.drive()
    seniorManager.write()

    val value: (String, Int) -> Int
    value = { str: String, num: Int ->
        println("num = $num, str = $str")
        num
    }
    value("the deep	dark fantasy", 233) + 1

    println(plus(233)(666))

    val value2: (String, Int) -> Int
    value2 = { str: String, num: Int ->
        println("num = $num, str = $str")
        num
    }
    println(value2("the deep dark fantasy", 233) + 1)

    val ls = java.util.ArrayList<Int>()
    ls.add(233)
    ls.add(666)
    ls.add(555)
    ls.add(1024)
    val sum = ls.fold(0) { sum, value ->
        sum + value
    }
    println(sum)

    val file = File("./save.png")
    file.openOrCreate()
    file.exists()

    println(makeFun().invoke())

//    val extensionLambda = Int.{ println(this) }
//    233.extensionLambda()    //	OK
//
//    extensionLambda(233)    //	OK

    println(fibonacci())
    var iterator = fibonacci().iterator()
    var value3 = iterator.next();
    while (value3 < 1000) {
        println("value = $value3")
        value3 = iterator.next();
    }

    val add5 = add(5)
    println(add5(2))

    val x = makeFun()
    x()
    x()
    x()
    x()

    val next = fibonacci2()
    for (i in 1..10) {
        println(next())
    }

    for (x in fibonacciGenerator()) {
        println(x)
        if (x > 100) break
    }
}

fun fibonacci2(): () -> Long {
    var first = 0L
    var second = 1L
    return fun(): Long {
        val result = second
        second += first
        first = second - first
        return result
    }
}

fun fibonacciGenerator(): Iterable<Long> {
    var first = 0L
    var second = 1L
    return Iterable {
        object : LongIterator() {
            override fun hasNext() = true

            override fun nextLong(): Long {
                val result = second
                second += first
                first = second - first
                return result
            }
        }
    }
}


fun applyToFile(block: File.() -> Unit) {
    val file = File("")
    file.block()    //	OK
    block(file)    //	OK
}

fun File.run(block: File.() -> Unit) {
    block()
}


fun File.openOrCreate() {
    if (!exists()) createNewFile()
}

fun <T> runWith(receiver: T, block: T.() -> Unit) {
    receiver.block()
}

fun plus(a: Int) = { b: Int -> a + b }

fun makeFun(): () -> Unit    //	()->Unit	函数是	makeFun()	的返回值，即函数的返回值是一个函数
//	makeFun()的函数体
{
    var count = 0
    return fun() {
        println(++count)
    }
}

fun fibonacci(): Iterable<Long> {
    var first = 0L
    var second = 1L
    return Iterable {
        object : LongIterator() {
            override fun nextLong(): Long {
                val result = second
                second += first
                first = second - first
                return result
            }

            override fun hasNext() = true
        }
    }
}

fun add(x: Int): (Int) -> Int {
    data class Person(val name: String, val age: Int)
    return fun(y: Int): Int {
        return x + y
    }
}