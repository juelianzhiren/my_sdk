package com.demo

fun main() {
    111.run {

    }

    // TODO 官方的：
    val r : Float = "Derry".let {
        it

        ""
        true
        9
        4354.545f
    }

    // 我们自己写一套
    val r2 : Boolean = "Derry".mLet {
        println("我的值是：$it")

        false
    }
    println(r2)
}

// 特点：let 与 run 区别只有一点   let"(I)"持有it        run"I.()"持有this
// 1. I.mLet 万能类型.mLet 所有类型都可以调用
// 2. -> O lambda里面最后一行是true，那就是Boolean
// 3.  (I)  让lambda持有it
private inline fun <I, O> I.mLet(lambda: (I) -> O) = lambda(this)