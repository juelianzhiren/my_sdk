package com.demo.grammer

class Person(private var name: String) {
    private var description: String? = null

    init {
        name = "Zhang Tao"
    }

    constructor(name: String, description: String) : this(name) {
        this.description = description
    }

    internal fun sayHello() {
        println("hello	$name")
    }
}
