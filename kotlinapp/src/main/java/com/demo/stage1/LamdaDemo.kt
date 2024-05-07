package com.demo.stage1

fun main() {
    val count1 = "dsfds".count()
    println(count1)

    val count2 = "dsfds".count { it == 'd' }
    println(count2)

    val methodAction : (String) -> String
    methodAction = { it ->
        "${it} ztq"
    }

    println(methodAction("a"))
    println(methodAction.invoke("a"))

    val methodAction2 : (Int, Int, Int) -> String = { number1, number2, number3 ->
        "methodAction2, 参数一：${number1}, 参数二：${number2}, 参数三:${number3}"
    }

    println(methodAction2(111, 222, 333))
    println(methodAction2.invoke(111, 222, 333))

    val methodAction3 = { v1: Int, v2:Float, v3: String ->
        "v1=$v1, v2=$v2, v3=$v3"
    }
    println(methodAction3(111, 44.4f, "abc"))
}