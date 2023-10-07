package com.demo.kotlinapp

class OperatorDemo {
}

data class Point(val x: Int, val y: Int)

operator fun Point.unaryMinus() = Point(-x, -y)

fun main() {
    val p = Point(1, 1)
    val np = -p
    println(np)
}