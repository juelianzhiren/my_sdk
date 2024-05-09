package com.demo.stage1

fun main() {
    val set: Set<String> = setOf("lisi", "wangwu", "zhaoliu", "zhaoliu") // set集合不会出现重复元素的
    println(set)
    // set[0] 没有这样 [] 的功能 去Set集合元素
    println(set.elementAt(0)) // [0]
    println(set.elementAt(1))
    println(set.elementAt(2))
    // println(set.elementAt(3)) // [3] 奔溃 会越界
    // println(set.elementAt(4)) // [4] 奔溃 会越界

    println()

    // 使用 list 或 set 集合，尽量使用  此方式，防止越界奔溃异常
    println(set.elementAtOrElse(0) {"越界了"})
    println(set.elementAtOrElse(100) {"越界了了"})

    println(set.elementAtOrNull(0))
    println(set.elementAtOrNull(111))
    // OrNull + 空合并操作符  一起使用
    println(set.elementAtOrNull(88) ?: "你越界啦啊")

    val set2 : MutableSet<String> = mutableSetOf("李元霸", "李连杰")
    set2 += "李俊"
    set2 += "李天"
    set2 -= "李连杰"
    set2.add("刘军")
    set2.remove("刘军")
    println(set2)
}