package com.demo.grammer

import android.util.Log

open class MyTestOne(var name: String) {
    init {
        println("MyTestOne init")
    }

    constructor(age: Int) : this("test") {
        println("MyTestOne construct 1")
    }

    constructor(age: Int, name: String) : this(age) {
        println("MyTestOne construct 2 $name")
    }
}

fun main() {
    val test1: MyTestOne = MyTestOne("name") //ok
    val test2: MyTestOne = MyTestOne(1)  //ok
    val test3: MyTestOne = MyTestOne(1,"wf") //ok
}