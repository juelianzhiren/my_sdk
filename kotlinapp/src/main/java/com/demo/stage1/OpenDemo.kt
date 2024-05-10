package com.demo.stage1

import java.io.File

// KT所有的类，默认是final修饰的，不能被继承，和Java相反
// open：移除final修饰
open class Person(private val name: String) {

    private fun showName() = "父类 的姓名是【$name】"

    // KT所有的函数，默认是final修饰的，不能被重写，和Java相反
    open fun myPrintln() = println(showName())
}

class Student(private val subName: String) : Person(subName) {

    private fun showName() = "子类 的姓名是【${subName}】"

    override fun myPrintln() = println(showName())

}

open class Person2(private val name: String) {

    fun showName() = "父类 的姓名是【$name】"

    // KT所有的函数，默认是final修饰的，不能被重写，和Java相反
    open fun myPrintln() = println(showName())
}

class Student2(private val subName: String) : Person2(subName) {

    fun showName2() = "子类 的姓名是【${subName}】"

    override fun myPrintln() = println(showName2())

}
open class Person3(val name: String) {
    private fun showName() = "父类显示:$name"

    open fun myPrintln() = println(showName())

    fun methodPerson() = println("我是父类的方法...") // 父类独有的函数
}

class Student3(val nameSub: String) : Person3 (nameSub) {
    override fun myPrintln() = println("子类显示:$nameSub")

    fun methodStudent() = println("我是子类的方法...") // 子类独有的函数
}


// TODO 83.Kotlin语言的继承与重载的open关键字学习
// 1.父类 val name  showName()->String  myPrintln->Unit
// 2.子类 myPrintln->Unit
fun main() {
    val person: Person = Student("张三")
    person.myPrintln()

    val p: Person2 = Student2("王五")
    p.myPrintln()

    println(p is Person2)
    println(p is Student2)
    println(p is File)

    // is + as = 一般是配合一起使用
    if (p is Student2) {
        (p as Student2).myPrintln()
    }

    if (p is Person2) {
        // (p as Person2).myPrintln() // 因为子类重写了父类
        println((p as Person2).showName())
    }

    println()

    val p2 : Person3 = Student3("李四")
    (p2 as Student3).methodStudent()
    p2.methodStudent()
    p2.methodStudent()
    p2.methodStudent()

    // 智能类型转换：会根据上面 as 转成的类型，自动明白，你现在的类型就是上面的类型
}