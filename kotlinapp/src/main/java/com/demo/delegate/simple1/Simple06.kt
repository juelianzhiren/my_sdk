package com.demo.delegate.simple1

fun requestDownload() : String {
    println("requestDownload run 》》》》》》")
    Thread.sleep(2000L) // 模拟下载延时了
    return "恭喜你，下载完成了"
}

val responseData : String by lazy { requestDownload() }

// TODO 懒加载委托（只有在用的时刻，才会开始加载，这就是懒加载）
fun main() {
    println("准备工作中...")
    Thread.sleep(3000L)

    println("开始请求中")
    println(responseData) // responseData如果没有值，只有在用responseData的时候，才会加载，这就是懒加载
    println(responseData) // responseData如果有值了，会直接返回
    println(responseData) // responseData如果有值了，会直接返回

    // 你只要敢读取 responseData   点睛之笔 override val value: T  get() { 这里面开始逻辑 }
    // 为什么官方，可以做到，懒加载委托，本质在于 override val value: T get() {} 你在用的时候，才会get触发激活
}