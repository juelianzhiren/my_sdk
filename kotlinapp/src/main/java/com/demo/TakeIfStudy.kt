package com.demo

fun main() {
    // TODO 官方的： true 返回 Derry，  false会返回一个null
    val r = "Derry".takeIf {
        it

        false
        true
    }
    println(r)

    // TODO 我们自己写一套
    val r2 = "DDD".mTakeIf {
        true
        false
    }
    println(r2)
}

// 此函数的返回类型是什么 : I ?
private inline fun<I> I.mTakeIf(action: (I) -> Boolean) = if (action(this)) this else null