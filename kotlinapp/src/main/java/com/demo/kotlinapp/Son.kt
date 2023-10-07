package com.demo.kotlinapp

open class Father {
    open val firstName = "Chen"
    open val lastName = "Jason"

    fun ff() {
        println("FFF")
    }
}

class Son : Father {
    override var firstName = super.firstName
    override var lastName = "Jack"

    constructor(lastName: String) {
        this.lastName = lastName
    }

    fun love() {
        super.ff()    //	调用父类方法
        println(super.firstName + " " + super.lastName + " Love " + this.firstName + " " + this.lastName)
    }
}

fun main() {
    var son = Son("Harry")
    son.love()

}