package com.demo

class AndroidClickListener<T> {

    private val actions = arrayListOf<(T?) -> Unit>()
    private val values = arrayListOf<T?>()

    // (T)  it
    // T.()  this

    fun addListener(value: T?, action: (T?) -> Unit) {
        if (!actions.contains(action)) {
            actions += action // add 添加用户自定义的事件到集合
            values += value // add 添加用户自定义的数据到集合
        }
    }

    // 模拟点击事件 点击
    fun touchListeners() {
        if (actions.isEmpty()) {
            println("你还没有添加任何事件呀")
            return
        }

        actions.forEachIndexed { index, item ->
            item.invoke(values[index]) // 执行所有的元素 == lambda规则 == 用户自定义事件体
        }
    }
}

fun main() {
    // 模拟安卓点击事件
    val func = AndroidClickListener<String>()
    func.touchListeners() // 在没有添加任何事件的时候，触发点击事件看看，相当于点击button

    func.addListener("Derry1") {
        println("事件被触发了 执行了 值是:$it")
    }

    func.addListener("Derry2") {
        println("事件被触发了 执行了 值是:$it")
    }

    func.addListener("Derry3") {
        println("事件被触发了 执行了 值是:$it")
    }

    func.touchListeners()

    println()

    // 我想传递 事件
    func.addListener("Derry4", ::show1)

    var result: (Any?) -> Unit = ::show1

    // 异步在lambda想要实现泛型，基本上做不到，用Any代替
    val method01: (Any) -> Unit = {}

    // lambda无法玩泛型

    func.touchListeners()

    val v1 = ::a
}

fun <T> show1(value: T?) {
    println("com.demo.show1 事件函数 事件被触发了 执行了 值是:$value")
}

fun a() {}