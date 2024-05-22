package com.demo.delegate.simple1

class Simple09 {

    // 需求：让内部可以修改，让外部不可以修改

    var data: String = ""
        private set

    private fun showData() {
        data = "IS OK SUCCESSFUL"
    }

}

fun main1() {
    val simple = Simple09()
    // simple.data = "Update" // 不能修改，编译不通过
    println(simple.data) // 只能读取
}

// ===============================================

class Model {
    // val修饰符的意义： 是只读的， 但是现在却被修改了，这个就是设计缺陷，Java的话，目前还是有这个问题
    val data: MutableList<String> = mutableListOf() // MutableList可以修改的集合   List不可修改集合

    private fun load() {
        // 网络请求
        data.add("Hello")
    }
}

fun main2() {
    val model = Model()
    // 类的外部仍然可以修改data
    model.data.add("World")
}

// ===============================================

class Model2 {

    // TODO 需求：内部可以修改，  但是 外部只能读取

    // List不可修改集合
    val data : List<String>  by :: _data

    // MutableList可以修改的集合
    private val _data : MutableList<String> = mutableListOf()

    fun load() {
        _data.add("Hello") // 内部可以修改
    }
}

// :: 启用官方的委托，    不用:: 就需要自定义委托来解决 编译不通过
fun main() {
    // 外部不能修改，只能读取
    val model = Model2()
    // model.data.add("Hello") // 报错，不可修改集合，压根就没有add方法
    // model._data // 报错，被私有化了
    println(model.data)
}