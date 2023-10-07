package com.demo.kotlinapp

class Example

open class Base(p: Int) {
    open fun v() {}
    fun nv() {}
}

class Derived(p: Int): Base(p) {
    override fun v() {}
}

open class AnotherDerived(p: Int): Base(p) {
    final override fun v() {}
}

fun main() {

}