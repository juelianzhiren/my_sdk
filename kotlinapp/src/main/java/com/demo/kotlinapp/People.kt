package com.demo.kotlinapp

data class People(val name: String, val age: Int) {
    fun getMax(): Int {
        return age * 10 + name.length;
    }
}

fun getMaxSort(people: People): Int {
    return people.age * 10 + people.name.length
}

fun main() {
    val people = People("zyh", 10)
    val people1 = People("zyh1", 100)
    val peopleList = arrayListOf<People>(people1, people)
//    peopleList.sortBy { people -> people.getMax() }
    println(peopleList)
//    peopleList.sortBy(::getMaxSort)
    println(peopleList)
    peopleList.sortBy(People::getMax)
    println(peopleList)

//    var a: String //提示错误 需要初始化或者抽象
//    val b: String //提示错误 需要初始化或者抽象
//    lateinit var c: String //正确
//    lateinit val d: String //提示错误，不能修饰不可变属性
//    lateinit var e: String? //提示错误，不能修饰可空类型的属性
//    lateinit var f: Int //错误，不能修饰基本数据类型
}

//private val g: Person by lazy { Person() }//正确
//private var h: Person by lazy { Person() } //编译错误