package com.demo

import kotlin.concurrent.thread

fun main() {
    // TODO 官方的：
    thread {
        println("我是异步线程")
    }

    // TODO 自己的
    mThread {
        println("我是异步线程 ${Thread.currentThread().name}")
    }
}

private /*inline*/ fun mThread(
    start : Boolean = true,
    name: String ? = null,
    /*crossinline*/ runAction: () -> Unit  // crossinline 你的这个lambda不要给我内联 优化
) : Thread {
    val thread = object : Thread() {
        override fun run() {
            super.run()
            runAction()
        }
    }

    if (start) thread.start()

    // if (name != null) thread.name = name
    name ?.let { thread.name = name }

    return thread
}