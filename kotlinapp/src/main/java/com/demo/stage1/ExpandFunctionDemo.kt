package com.demo.stage1

import java.io.File
import java.nio.charset.Charset
import java.util.ArrayList

// 假设这个代码是，开源的，或者是很庞大JDK源码，或者是非常复杂的开源库
class KtBase113 (val name: String, val age: Int, val sex: Char)

// 增加扩展函数
fun KtBase113.show() {
    println("我是show函数, name:${name}, age:$age, sex:$sex")
}

// 增加扩展函数
fun KtBase113.getInfo() = "我是getInfo函数, name:${name}, age:$age, sex:$sex"

fun String.addExtAction(number: Int) =  this + "@".repeat(number)

fun String.showStr() = println(this)

/* 增加扩展函数后 的 背后代码

    public final class KtBase113Kt {

        public static final void com.demo.show(KtBase113 $this$com.demo.show) {
            System.out.println("我是show函数, name:" + $this$com.demo.show.name + ", age:" + $this$com.demo.show.age, sex:" + $this$com.demo.show.sex);
        }

        public static final void getInfo(KtBase113 $this$getInfo) {
            return "我是getInfo函数, name:" + $this$com.demo.show.name + ", age:" + $this$com.demo.show.age, sex:" + $this$com.demo.show.sex;
        }

        public static final void showStr(String $this$showStr) {
            System.out.println($this$showStr);
        }

        public static final String addExtAction(String $this$addExtAction) {
           return $this$addExtAction + StringsKt.repeat((CharSequence)"@", number);
        }

        public static void main(String [] args) {
            main();
        }

        public static void main() {
            // ...
        }
    }

 */



data class ResponseResult1(val msg: String, val code: Int)
data class ResponseResult2(val msg: String, val code: Int)
data class ResponseResult3(val msg: String, val code: Int)
data class ResponseResult4(val msg: String, val code: Int)
// 省略几亿个类 ....

// 最超类进行 一个函数 扩展
fun Any.showPrintlnContent() = println("当前内容是:$this")

fun Any.showPrintlnContent2() : Any {
    println("当前内容是:$this")

    return this
}


// 第一点：如果我们自己写了两个一样的扩展函数，编译不通过

// 第二点：KT内置的扩展函数，被我们重复定义，属于覆盖，而且优先使用我们自己定义的扩展函数
public fun File.readLines(charset: Charset = Charsets.UTF_8): List<String> {
    val result = ArrayList<String>()
    forEachLine(charset) { result.add(it); }
    return result
}


// KtBase113.xxx  xxx函数里面会持有this == KtBase113对象本身
// TODO 113-Kotlin语言的定义扩展函数学习
fun main() {
    val p = KtBase113("张三", 28, '男')
    p.show()
    println(p.getInfo())

    println("Derry".addExtAction(8))
    println("Kevin".addExtAction(3))

    "这个是我的日志信息".showStr()
    "Beyond".showStr()



    ResponseResult1("login success", 200).showPrintlnContent()
    ResponseResult2("login success", 200).showPrintlnContent()
    ResponseResult3("login success", 200).showPrintlnContent()
    ResponseResult4("login success", 200).showPrintlnContent()

    "Derry1".showPrintlnContent()
    "Kevin1".showPrintlnContent()
    val number1 = 999999
    number1.showPrintlnContent()
    val number2 = 645654.6
    number2.showPrintlnContent()
    val number3 = 544354.5f
    number3.showPrintlnContent()
    val sex = '男'
    sex.showPrintlnContent()

    println()

    '女'.showPrintlnContent2().showPrintlnContent2().showPrintlnContent2()
    "DerryOK".showPrintlnContent2().showPrintlnContent2().showPrintlnContent2().showPrintlnContent2()

    println(File("D:\\a.txt").readLines())
}