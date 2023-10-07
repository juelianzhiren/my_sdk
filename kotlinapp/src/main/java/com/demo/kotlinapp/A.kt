package com.demo.kotlinapp

open class A {
    open fun f() {
        println("A")
    }

    fun a() {
        println("a")
    }
}

interface B {
    fun f() {
        println("B")
    }

    fun b() {
        println("b")
    }
}

class C(): A(), B {
    override fun f() {
        super<A>.f()
        super<B>.f()
    }
}

fun main() {
    val c = C()
    c.f()

}