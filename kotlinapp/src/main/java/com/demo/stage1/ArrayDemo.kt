package com.demo.stage1

import java.io.File

fun main() {
    // 1.intArrayOf 常规操作的越界奔溃
    val intArray /*: IntArray*/ = intArrayOf(1, 2, 3, 4, 5)
    println(intArray[0])
    println(intArray[1])
    println(intArray[2])
    println(intArray[3])
    println(intArray[4])
    // println(intArray[5]) // 奔溃：会越界异常

    println()

    // 2.elementAtOrElse elementAtOrNull
    println(intArray.elementAtOrElse(0) { -1 })
    println(intArray.elementAtOrElse(100) { -1 })

    println(intArray.elementAtOrNull(0))
    println(intArray.elementAtOrNull(200))

    // OrNull + 空合并操作符 一起来用
    println(intArray.elementAtOrNull(666) ?: "你越界啦啊啊啊")

    println()

    // 3.List集合转 数组
    val charArray /*: CharArray*/ = listOf('A', 'B', 'C').toCharArray()
    println(charArray)

    // 4.arrayOf Array<File>
    val objArray /*: Array<File>*/ = arrayOf(File("AAA"), File("BBB"), File("CCC"))

//    val notActualPeople: Array <Person?> = arrayOfNulls <Person> (13)
}