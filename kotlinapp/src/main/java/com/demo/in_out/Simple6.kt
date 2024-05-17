package com.demo.in_out

open class Person {
    override fun toString(): String {
        return "Person ..."
    }
}

class Student2 : Person() {
    override fun toString(): String {
        return "Student2..."
    }
}

class Teacher : Person() {
    override fun toString(): String {
        return "Teacher..."
    }
}

// Dog不参与任何继承关系，他是其他类
class Dog {
    fun show1() = println("Dog show1 run ...")
    fun show2(str: String) = println("show2 run str:$str")
}

// 非常欠打的invoke  只能是 Person 与 Person的子类
fun <T : Person> myInvoke1(item: T) = println(item) // 会自动调用 toString函数
fun <T : () -> Unit> myInvoke2(item: T) = item.invoke()
fun <T : (String) -> Unit> myInvoke3(item: T) = item.invoke("Derry")
fun <T> myInvoke4(item: T) where T : () -> Unit, T : (String) -> Unit {
    item.invoke(); item.invoke("DDD")
}

fun main() {
    // myInvoke1(Any()) // 报错，操作限定的范围
    myInvoke1(Person())
    myInvoke1(Teacher())
    myInvoke1(Student2())
    // myInvoke1(Dog()) // 报错，操作限定的范围

    println()

    myInvoke2(Dog()::show1)

    println()

    myInvoke3(Dog()::show2)

    println()

    /*myInvoke4(Dog()::show1) // 报错：无法确定到底那个函数， 编译期不够智能
    myInvoke4(Dog()::show2) // 报错：无法确定到底那个函数， 编译期不够智能*/
}