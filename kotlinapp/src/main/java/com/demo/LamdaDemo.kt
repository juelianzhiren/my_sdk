package com.demo

fun main() {
    // lambda会慢慢的难度升级

    // Unit == java的void

    // TODO 下面全部都是函数声明， 既然是函数声明，就不能调用

    // 函数的声明 用lambda去描述函数的声明
    var method1 : () -> Unit

    var method2 : (Int, Int) -> Unit

    var method3 : (String, Double) -> Any

    var method4 : (Int, Double, Long, String) -> Boolean

    var method5 : (Int, Int) -> Int

    // 函数的声明 + 函数的实现

    // : (参数)->返回  方式一 () 基本上属于声明 todo 上面已经讲过了 就是函数的声明

    // = {参数->方式}  方式二 () 基本上属于声明实现结合
    var method01 /*: () -> Unit*/ = { println("我是method01函数") }
    method01() // 调用函数  () == 操作符重载 invoke操作符
    method01.invoke()

    var method02 = { "我是method02函数" }
    println(method02())

    var method03 = {str: String -> println("你传递进来的值是:$str")}
    method03("Derry")

    var method04 = {number1: Int, number2: Int -> number1.toString() + number2.toString()}
    println(method04(1, 2))

    var method05 = {number1: Int, number2: Int -> number1 + number2}
    println(method05(1, 2))

    var method06 : (Int)->String // 先声明
    method06 = fun(value : Int) : String= value.toString() // 后实现
    println(method06(99))

    // 用 先声明 的类型，来 自动推断
    var method07 : (Int)->String // 先声明
    method07 = fun(value)  = value.toString() // 后实现
    println(method07(99))

    // 声明 + 实现 一气呵成
    var method08 : (Int) -> String/*左边是声明*/           /*右边是实现*/= {value-> "$value aaa"}
    println(method08(77))

    var method09 : (String, String) -> Unit = {aStr, bStr -> println("aStr:$aStr, bStr:$bStr")}
    method09("AAA", "BBB")

    var method10 : (String) -> Unit = { /*it ->*/ // 如果你只有一个参数的话，如果你不写，默认就有一个it，他自动会给你增加
        println("你传递进来的值是:$it")
    }
    method10("李元霸")

    var method11 : (Int) -> Unit = {
        when(it) {
            1 -> println("你传递是一")
            in 20..60 -> println("你传递是 20~60之内的数字")
            else -> println("都不满足")
        }
    }
    method11(600)

    /*var method11s : (Int) -> String = {
        when(it) {
            1 -> "你传递是一"
            in 20..60 -> "你传递是 20~60之内的数字"
            else -> "都不满足"
        }
    }
    println(method11s(666))*/

    // _ 拒收
    var method13 : (Int, Int) -> Unit = {_, number2 ->
        println("你传递的第二个参数是：$number2")
    }
    method13.invoke(100, 200)

    var method14 /* : (Any) -> Any */ = {str: Any -> str}
    println(method14(true))
    println(method14(54.45f))

    // === 引用对象的比较    ==值的比较
    var method15 /*: (Char) -> Unit*/ =
        {sex: Char -> if (sex == '男') println("你是男生") else if (sex == '女') println("你是女生") else println("未知人类")}
    method15('男')

    var method16 /*: (Int)->Unit */  = {number: Int -> println("你的值是:$number")}

    // 虽然你覆盖了，类型还是  (Int)->Unit
    method16 = { println("????你的值是什么:$it") } // 覆盖你 method16
    method16(888)


    var method16ss : (String? /*= ""*/, String) -> Unit = {str /*= "default"*/, str2 ->
        println("str:$str, str2:$str2")
    }
    method16ss("李元霸", "李连杰")
    method16ss(null, "李连杰")

    var method16sss = {str: String /*= "default"*/, str2: String ->
        println("str:$str, str2:$str2")
    }
    method16ss("李元霸", "李连杰")
    method16ss(null, "李连杰")

    // 需求：你传入什么，我就打印什么 并且输出
    var method17 = {str: Any ->
        println("你传入了:$str")
        str  // 最后一行最为返回值
    }
    println(method17(34534))
    println(method17('C'))
    println(method17("DDDD"))

    // String.()  给String类，增加一个匿名函数 == 效果： 我们的 lambda体会持有String本身 == this
    var method19 : String.() -> Unit = {
        // this == String本身
        println("你是:$this")
    }
    "DDD".method19()

    var method20 : Int.(Int) -> String = { "两数相加结果是:${this + it}" }
    println(1.method20(2))
    println(method20(1, 2))

    var method21 : Double.(Double, Double) -> Unit = {d1, d2 -> println("三数相加结果是:${this + d1 + d2}")}
    method21.invoke(55.5, 66.6, 77.7)
    55.5.method21(66.6, 77.7)

    var method22 : Char.(String) -> Unit
            = { println(if (this == '男') "先生你好" else if (this == '女') "女士你好" else it)}
    'A'.method22("未知人类")
    method22('女', "未知人类")

    fun Char.method23(default: String) {
        println(if (this == '男') "先生你好" else if (this == '女') "女士你好" else default) }
    '男'.method23("未知人类")

    // TODO 我们前面一直在玩 参数 输入，  现在我们玩输出(返回类型)
    fun t01() { println(1)} // 默认Unit，除非指定类型
    fun t02() {54656.45} // 默认Unit
    fun t03() {true} // 默认Unit
    fun t04() : String { return "Derry" } // 默认String
    fun t05() : Int { return 99 } // 在函数体里面 return xxx，是无法类型推到的

    fun s01() /*: () -> Unit*/ = {} // () -> Unit
    fun s02() /*: () -> Unit*/ = { println("OK") }  // () -> Unit

    // // s03和s04都有入参的时候要怎么调用
    // run { 执行你的函数 show1 show2  }
    fun s03() : Boolean = run { true } // Boolean    run返回 {}里面的函数返回类型
    fun s04() : ()-> Boolean = { true } // () -> Boolean    输入 -> 输出
    s03()
    s04()()

    // (Int)->Unit
    fun k01() /*: (Int)->Unit*/ = {n1: Int -> println("你的输入类型是int? ${if (n1 is Int) "你是Int类型:$n1" else "你不是Int"}")}
    k01()(88) // (88) 执行 你函数返回的函数

    // (Int,Int) -> Char
    fun k02() : (Int,Int) -> Char = {n1: Int, n2: Int -> println("两数相加:${n1 + n2}")
        true
        'A'
    } // 划重点，想写多行直接敲回车，为什么要换行：为了编译器检查

    // 一般是 lambda + 函数 == 高级函数

    var age : Int = 19

    // fun aa{} 与 var aa={}有啥区别
    fun aa() {}
    var aa2 = {}
    // aa就是一个函数，实打实的函数
    // aa2 是接收一个匿名函数的变量而已，这个变量 可以执行这个匿名函数
    // 共同点：他们现象是一样的
    var aa3 = aa2 // 都属于函数引用的概念
    var aa4 = ::aa // 实打实的函数 变成 函数引用 所以可以传递赋值 给另外一个变量

    // 前面为什么用var方法不应该是fun吗？
    // 我们一直在写匿名函数，把匿名函数给 method01 变量
    // KT函数是一等公民

    // 全栈语言： 服务器，iOS，Android，Windows，JS，Gradle Groovy  build.gradle.kts ...
    // native层

    // 函数中的函数，就是高阶函数

    // KT中的 基本上都是表达式 包括 if  可以灵活返回
    // Java中，基本上都是 语句 包括 if  执行体 不可以返回

    var a = if (true) 111 else 222

    // "ABCDE".shows()
}

fun show(name: String = "default") {

}

// public static shows
// KT默认是公开的public
fun String.shows() {
    // String == this
    println("持有的String本身是:$this")
}

/*
fun Glide.show() {
    // this == Glide 本身
}

fun OkHttp.showInfo() {
    // this == OkHttp本身
}*/

// public static Object toast(toast$this)
fun Any.toast() {
    // Any == this
    println("你个货调用了我，你的值是:$this")
}

// 你对xxx扩展  this==xxx本身

// 函数声明  =  函数实现{}
// fun 函数声明  函数实现{}


fun info() /*: Unit*/ {

}