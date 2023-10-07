package com.demo.grammer

abstract class Person1(open val age: Int) {
    abstract fun work()
}

class MaNong(age: Int): Person1(age) {
    override val age: Int
        get() = 0

    override fun work() {
        println("我是码农，我在写代码")
    }
}

class Doctor(age: Int): Person1(age) {
    override fun work() {
        println("我是医生，我在给病人看病")
    }
}

fun main() {
    val person: Person1 = MaNong(23)
    person.work()
    println(person.age)

    val person2: Person1 = Doctor(24)
    person2.work()
    println(person2.age)
}