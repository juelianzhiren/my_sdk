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

    loginAPI3("Derry", "123456") { msg: String, code: Int ->
        println("最终登录的情况如下: msg:$msg, code:$code")
    }

    val obj = ::methodResponseResult

    loginAPI3("Derry", "123456", obj)
}

fun methodResponseResult(msg: String, code: Int) {
    println("最终登录的成果是:msg:$msg, code:$code")
}

// 模拟：数据库SQLServer
const val USER_NAME_SAVE_DB3 = "Derry"
const val USER_PWD_SAVE_DB3 = "123456"

// 此函数有使用lambda作为参数，就需要声明成内联
// 如果此函数，不使用内联，在调用端，会生成多个对象来完成lambda的调用（会造成性能损耗）
// 如果此函数，使用内联，相当于 C++ #define 宏定义 宏替换，会把代码替换到调用处，调用处 没有任何函数开辟 对象开辟 的损耗
// 小结：如果函数参数有lambda，尽量使用 inline关键帧，这样内部会做优化，减少 函数开辟 对象开辟 的损耗

// 登录API 模仿 前端
public inline fun loginAPI3(username: String, userpwd: String, responseResult: (String, Int) -> Unit) {
    if (username == null || userpwd == null) {
        TODO("用户名或密码为null") // 出现问题，终止程序
    }

    // 做很多的校验 前端校验
    if (username.length > 3 && userpwd.length > 3) {
        if (wbeServiceLoginAPI3(username, userpwd)) {
            // 登录成功
            // 做很多的事情 校验成功信息等
            // ...
            responseResult("login success", 200)
        } else {
            // 登录失败
            // 做很多的事情 登录失败的逻辑处理
            // ...
            responseResult("login error", 444)
        }
    } else {
        TODO("用户名和密码不合格") // 出现问题，终止程序
    }
}

// 此函数没有使用lambda作为参数，就不需要声明成内联
// 登录的API暴露者 服务器
fun wbeServiceLoginAPI3(name: String, pwd: String) : Boolean {
    // kt的if是表达式(很灵活)     java的if是语句(有局限性)

    // 做很多的事情 登录逻辑处理
    // ...

    return name == USER_NAME_SAVE_DB3 && pwd == USER_PWD_SAVE_DB3
}