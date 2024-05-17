package com.demo.in_out

// 这个类，就是只能修改, 不能获取
// 声明的时候加入  一劳永逸了<in T>
class Student<in T> {

    /*fun a1(list: MutableList<in T>) {

    }

    fun a2(list: MutableList<in T>) {

    }

    fun a3(list: MutableList<in T>) {

    }

    fun a4(list: MutableList<in T>) {

    }

    fun a5(list: MutableList<in T>) {

    }*/

    fun setData(data: T) {}

    fun addData(data: T) {}

    // 不能获取
    // fun getData() : T
}

// TODO ------------------------------------------------

// 这个类，就是只能获取，不能修改了
// 声明的时候加入  一劳永逸了<out T>
class Worker<out T> {

    // 能获取
    fun getData(): T? = null

    // 不能修改
    /*fun setData(data: T) {

    }

    fun addData(data: T) {

    }*/
}