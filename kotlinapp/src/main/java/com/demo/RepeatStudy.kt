package com.demo

fun main() {
    // TODO 官方的：
    repeat(10) {
        print("下标是:$it ")
    }

    println()

    // TODO 我们自己手写
    mRepeat(10) {
        print("下标是:$it ")
    }

    println()

    10.mRepeat2 {
        print("下标是:$it ")
    }
}

private inline fun mRepeat(count : Int, action: (Int) -> Unit) {
    for (item in 0 until count) action(item)
}

private inline fun Int.mRepeat2(action: (Int) -> Unit) {
    for (item in 0 until this) action(item)
}