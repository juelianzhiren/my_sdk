package com.demo.stage1

// username: String, userage: Int, usersex: Char  临时类型，必须要二次转换，才能用
class KtBase76 (username: String, userage: Int, usersex: Char) // 主构造
{
    // 这个不是Java的 static{}
    // 相当于是Java的 {} 构造代码块
    // 初始化块  init代码块
    init {
        println("主构造函数被调用了 $username, $userage, $usersex")
        // 如果第一个参数是false，就会调用第二个参数的lambda
        // 判断name是不是空值 isNotBlank   ""
        require(username.isNotBlank()) { "你的username空空如也，异常抛出" }
        require(userage > 0) { "你的userage年龄不符合，异常抛出" }
        require( usersex == '男' || usersex == '女') { "你的性别很奇怪了，异常抛出" }
    }

    constructor(username: String) : this(username, 87, '男') {
        println("次构造函数被调用了")
    }

    fun show() {
//         println(username) // 用不了，必须要二次转换，才能用
    }
}

// 第一步：生成val sex: Char
class KtBase77(_name: String, val sex: Char) // 主构造
{

    // 第二步： 生成val mName  // 由于你是写在 init代码块前面，所以先生成你， 其实类成员 和 init代码块 是同时生成
    val mName = _name

    init {
        val nameValue = _name // 第三步：生成nameValue细节
        println("init代码块打印:nameValue:$nameValue")
    }

    // 次构造 三个参数的  必须调用主构造
    constructor(name: String, sex: Char, age: Int) :this(name, sex) {
        // 第五步：生成次构造的细节
        println("次构造 三个参数的, name:$name, sex:$sex, age:$age")
    }

    // 第四步
    val derry = "AAA"

    // 纠正网上优秀博客的错误： 类成员先初始生成   再init代码块初始生成  错误了
    // Derry正确说法：init代码块 和 类成员 是同时的，只不过你写在 init代码块前面 就是先生成你
}

// TODO 76.Kotlin语言的初始化块学习
// 1.name,age,sex的主构造函数
// 2.init代码块学习 require
// 3.临时类型只有在 init代码块才能调用
fun main() {
    KtBase76("李四", userage = 88, usersex = '女')  // 调用主构造
    println()
    KtBase76("王五") // 调用次构造

    // KtBase76("") // 调用次构造
    // KtBase76("李四", userage = -1, usersex = 'M')  // 调用主构造

    KtBase76("李四", userage = 1, usersex = '男')  // 调用主构造


    println()
    // 调用次构造
    KtBase77("李元霸", '男', 88)  // 调用次构造
}