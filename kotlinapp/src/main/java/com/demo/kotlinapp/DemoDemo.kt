package com.demo.kotlinapp

open class Fruit

class Apple : Fruit()

class Banana : Fruit()

interface Consumer<in T> {
    fun consume(item: T)
}

fun main() {
    val fruitConsumer: Consumer<Fruit> = object : Consumer<Fruit> {
        override fun consume(item: Fruit) {
            println("Consuming fruit: $item")
        }
    }

    val appleConsumer: Consumer<Apple> = object : Consumer<Apple> {
        override fun consume(item: Apple) {
            println("Consuming apple: $item")
        }
    }

    val bananaConsumer: Consumer<Banana> = object : Consumer<Banana> {
        override fun consume(item: Banana) {
            println("Consuming banana: $item")
        }
    }

    // 使用逆变，可以将子类型对象赋值给父类型的引用
    fruitConsumer.consume(Apple())   // 输出：Consuming fruit: Apple@xxxxx
    fruitConsumer.consume(Banana())  // 输出：Consuming fruit: Banana@xxxxx

    // 不能将父类型对象赋值给子类型的引用
    // appleConsumer.consume(Fruit())  // 编译错误
    // bananaConsumer.consume(Fruit()) // 编译错误
}