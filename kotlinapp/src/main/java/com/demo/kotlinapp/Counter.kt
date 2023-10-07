package com.demo.kotlinapp

data class Counter (val index: Int)

operator fun Counter.plus(increment: Int) : Counter {
    return Counter(index + increment)
}

fun main() {
    val c = Counter(1)
    val cplus = c + 10
    println(cplus)
}