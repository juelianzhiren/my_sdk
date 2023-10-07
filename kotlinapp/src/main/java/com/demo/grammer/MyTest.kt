package com.demo.grammer

import android.util.Log

class MyTest private constructor(name: String) {
    var name: String = ""
    init {
        this.name = name;
        Log.v("MyTest", "this name is ${this.name}")
    }

    constructor(name: String, age: Int):this(name) {

    }

    init {
        Log.v("MyTest", "init 2")
    }
    val testName = this.name.toString()
}

class MyTest1(name: String) {

}