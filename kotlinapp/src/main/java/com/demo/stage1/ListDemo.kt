package com.demo.stage1

import android.os.Build
import android.support.annotation.RequiresApi

fun main() {
    val list = listOf("Derry", "Zhangsan", "Lisi", "Wangwu")

    // 普通取值方式：    索引  内部是运算符重载 [] == get
    println(list[0])
    println(list[1])
    println(list[2])
    println(list[3])
    // println(list[4]) // 奔溃  java.lang.ArrayIndexOutOfBoundsException: 4

    println()

    // 我们写KT代码，一定不会再出现，空指针异常，下标越界异常
    // 防止奔溃取值方式： getOrElse getOrNull
    println(list.getOrElse(3) {"越界"})
    println(list.getOrElse(4) {"你越界了"})
    println(list.getOrElse(4402) {"你越界了啊"})

    println()

    println(list.getOrNull(1))
    println(list.getOrNull(4))
    println(list.getOrNull(111))
    // getOrNull + 空合并操作符
    println(list.getOrNull(222) ?: "你越界了哦哦")

    // 小结：开发过程中，尽量使用 getOrElse 或 getOrNull，才能体现KT的亮点

    useMutableList()
}

@RequiresApi(Build.VERSION_CODES.N)
fun useMutableList() {
    // 可变的集合
    val list = mutableListOf("Derry", "Zhangsna", "Wangwu")
    // 可变的集合，可以完成可变的操作
    list.add("赵六")
    list.remove("Wangwu")
    println(list)

    // 第一种 遍历方式：
    for (i in list) {
        print("元素:$i  ")
    }

    println()

    // 第二种 遍历方式：
    list.forEach {
        // it == 每一个元素
        print("元素:$it  ")
    }

    println()

    // 第三种 遍历方式：
    list.forEachIndexed { index, item ->
        print("下标:$index, 元素:$item    ")
    }

    println()

    val(value1, value2, value3) = list
    // value1 = ""  val只读的
    println("value1:$value1, value2:$value2, value3:$value3")

    var(v1, v2, v3) = list
    // v1 = "OK"
    println("v1:$v1, v2:$v2, v3:$v3")

    // 用_内部可以不接收赋值，可以节约一点性能
    val(_ , n2, n3) = list
    // println(_) _不是变量名，是用来过滤解构赋值的，不接收赋值给我
    println("n2:$n2, n3:$n3")

    // 不可变集合 to 可变集合
    val list2 = listOf(123, 456, 789)
    // 不可以的集合，无法完成可变的操作
    // list2.add
    // list2.remove

    val list3 : MutableList<Int> = list2.toMutableList()
    // 可变的集合，可以完成可变的操作
    list3.add(111)
    list3.remove(123)
    println(list3)

    // 可变集合 to 不可变集合
    val list4: MutableList<Char> = mutableListOf('A', 'B', 'C')
    // 可变的集合，可以完成可变的操作
    list4.add('Z')
    list4.remove('A')
    println(list4)

    val list5: List<Char> = list4.toList()
    // 不可以的集合，无法完成可变的操作
    /*list5.add
    list5.remove*/


    // 1.mutator += -= 操作
    val list0 : MutableList<String> = mutableListOf("Derry", "DerryAll", "DerryStr", "Zhangsan")
    list0 += "李四" // mutator的特性 +=  -+ 其实背后就是 运算符重载而已
    list0 += "王五"
    list0 -= "Derry"
    println(list0)

    // 2.removeIf
    // list.removeIf { true } // 如果是true 自动变量整个可变集合，进行一个元素一个元素的删除
    list0.removeIf { it.contains("Derr") } // 过滤所有的元素，只要是有 Derr 的元素，就是true 删除
    println(list0)
}