package com.demo.stage1

const val PI = 3.1415926;

fun main() {
    val ch : String? = null

    ch?.length

    val a = 101;

    if (a in 0..59) {
        println("不及格")
    } else if (a in 60..100) {
        println("及格了")
    } else if (a !in 0..100) {
        println("无效的分数")
    }

    val day = 0;
    var result = when(day) {
        1 -> println("星期一")
        2 -> println("星期二")
        3 -> println("星期三")
        4 -> println("星期四")
        5 -> println("星期五")
        6 -> println("星期六")
        7 -> println("星期天")
        else -> {
            "非法的星期几"
        }
    }
    println(result)

    val garden = "黄石公园"
    val time = 6

    println("今天天气很晴朗，去玩" + garden + "，玩了" +time +" 小时") // Java的写法
    println("今天天气很晴朗，去${garden}玩，玩了$time 小时") // 字符串模版的写法

    // KT的if是表达式，所以可以更灵活，  Java的if是语句，还有局限性
    val isLogin = false
    println("server response result: ${if (isLogin) "恭喜你，登录成功√" else "不恭喜，你登录失败了，请检查Request信息"}")

    login(password = "234", username = "ztq")

    Base.`in`()
    Base.`is`()
    `123123adfsas我都收到了`()

    when (a) {
        -1 -> TODO("分数不合法")
        in 0..59 -> println("不及格")
        in 60..100 -> println("及格")
        else -> TODO("分数不合法2")
    }
    ch!!.length
}

fun sum(a : Int, b: Int) : Int {
    return a + b
}

fun sum1(a : Int, b: Int) = a + b

fun sum2(a : Int, b: Int) : Int = a + b

fun login(username: String, password : String) {
    println("usernam is ${username}, password is ${password}")
}

fun `123123adfsas我都收到了`() :Unit {
    println("特殊函数")
}