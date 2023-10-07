package com.demo.kotlinapp

//val <T> List<T>.lastIndex: Int get() = size - 1
val <T> List<T>.lastIndex: Int get() = size - 1

fun String.notEmpty(): Boolean {
    return !this.isEmpty()
}

fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

fun <T> MutableList<T>.mswap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

class ExtensionDemo {
    fun useExtensions() {
        val a = "abc"
        println(a.notEmpty())

        val mList = mutableListOf<Int>(1,2,3,4,5)
        println("Before swap")
        println(mList)
        mList.swap(0, mList.size - 1)
        println("After swap")
        println(mList)

        val mmList = mutableListOf<String>("a12", "b34", "c56", "d78")
        println("Before swap")
        println(mmList)
        mmList.mswap(1, 2)
        println("After swap")
        println(mmList)

        val mmmList = mutableListOf<Int>(100, 200, 300, 400, 500, 600)
        println("Before swap:")
        println(mmmList)
        mmmList.mswap(0, mmmList.lastIndex)
        println("After swap:")
        println(mmmList)
    }

    fun useExtensions2() {
        val mmmList = mutableListOf<Int>(100, 200, 300, 400)
        println(mmmList.lastIndex)
    }
}

fun main() {
    val demo = ExtensionDemo();
    demo.useExtensions()
    println("---------------------")
    demo.useExtensions2()
}