// TODO 高阶函数第三版

import java.util.function.BinaryOperator

const val nameS : String = "Derry"
const val ageS : Int = 99
fun commonOK() = /*println("我是通用函数")*/ 88
// 省略一万个类型
// ...

// TODO 高阶函数第三版  高阶函数+扩展函数=比较强大的功能
fun main() {
    val r : Boolean = commonOK().myRunOK {

        println(this)
        println(it)
        true
    }
    // println(r)

    123.myRunOK {
        // this == T本身 == 123
        println(it)
        false
    }

    'A'.myRunOK {
        println(this)
        true
    }

    "derry".run {
        length // 已经等到derry字符串长度了
    }

    "derry".let {
        it.length // 已经等到derry字符串长度了
    }


    // 手写html解析语言，相当于手写一门语言
    // 这个才算的上Kotlin写法
    /*html {
        head{
           title("")
        }
        body {
           p {

           }

           a {
           }
        }
    }*/

}

// t.()的this,和(t)的it有什么区别?
// T.()  {持有this == T本身}
// (T)  {持有it == T本身}
// (Double) {持有it == Double本身}

// fun <T> T.myRunOK( mm: (Float) -> Boolean ) /*: Boolean*/ = mm(656.4f) // 执行lambda

// 我对T扩展，T本身就等于==this
fun <T> T.myRunOK(mm: T.(Double) -> Boolean) /*: Boolean*/ = mm(664.45) // 执行lambda


// 难点： 第一个this 调用端
//       第二个this  lambda里面端

// 给整个项目用，来一个扩展函数 默认：public static
// 想给所有类型用， String Char Boolean ... 泛型
// fun <T> T.myRunOK( mm: T.() -> Boolean ) /*: Boolean*/ = mm() // 执行lambda

// 你对xxx扩展  this==xxx本身
// 你对函数扩展，持有函数本身，其实是没有错误的，但是更准确来说，this持有函数的返回类型

// T.() 会给我们的 mm的lambda   就会产生  lambda实现体里面持有this == T本身 == 123.T 所以this等于123
// (T)                                 lambda实现体里面持有it == T本身 == 123.T 所以this等于123

// Myrunok和mm的区别是什么   mm就是lambda    Myrunok是泛型的函数扩展而已
