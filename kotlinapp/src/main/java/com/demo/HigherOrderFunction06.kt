package com.demo

// TODO 高阶函数第四版
fun main() {
    // 历史演化过程： 1 2 3 4
    show("张三", lambda = {
        println("输出:$it")
    })

    show("李四", {
        println("输出:$it")
    })

    show("王五") {
        println("输出:$it")
    }

    show {
        println("输出:$it")
    }

    // 使用 多lambda
    show2(lambda1 = {
        println("lambda1输出:$it")
    }, lambda2 = {
        println("lambda2输出:$it")
    })

    show2({
        println("lambda1输出:$it")
    },{
        println("lambda2输出:$it")
    })

    show2({
        println("lambda1输出:$it")
    }) {
        println("lambda2输出:$it")
    }

    // 明确的指定，能不能顺序调换
    show2(lambda2 = {
        println("lambda2输出:$it")
    }, lambda1 = {
        println("lambda1输出:$it")
    })

    show3(sex_ = '女', name = "Derry", age = 99, sex = 'n')
}

fun show(name: String = "Derry", lambda: (String) -> Unit) = lambda(name)

// 开启 多lambda
fun show2(lambda1: (Int) -> Unit, lambda2: (Int) -> Unit) {
    lambda1(100)
    lambda2(222)
}

fun show3(name: String, age: Int, sex: Char, sex_ : Char) {

}