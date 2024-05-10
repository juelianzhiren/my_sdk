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

class KtBase78 {

    // lateinit val AAA; // AAA 无法后面在修改了，我还怎么延时初始化？
    lateinit var responseResultInfo: String // 我等会儿再来初始化你，我先定义再说，所以没有赋值

    // 模拟服务器加载
    fun loadRequest() { // 延时初始化，属于懒加载，用到你在给你加载
        responseResultInfo = "服务器加载成功，恭喜你"
    }

    fun showResponseResult() {
        // 由于你没有给他初始化，所以只有用到它，就奔溃
         if (responseResultInfo == null) println()
        // println("responseResultInfo:$responseResultInfo")

        if (::responseResultInfo.isInitialized) {
            println("responseResultInfo:$responseResultInfo")
        } else {
            println("你都没有初始化加载，你是不是忘记加载了")
        }
    }
}

class KtBase79 {

    // >>>>>>>>>>>>>>>>>>> 下面是 不使用惰性初始化 by lazy  普通方式(饿汉式 没有任何懒加载的特点)
    // val databaseData1 = readSQlServerDatabaseAction()

    // >>>>>>>>>>>>>>>>>>> 使用惰性初始化 by lazy  普通方式
    val databaseData2 by lazy { readSQlServerDatabaseAction() }

    private fun readSQlServerDatabaseAction(): String {
        println("开始读取数据库数据中....")
        println("加载读取数据库数据中....")
        println("加载读取数据库数据中....")
        println("加载读取数据库数据中....")
        println("加载读取数据库数据中....")
        println("加载读取数据库数据中....")
        println("加载读取数据库数据中....")
        println("加载读取数据库数据中....")
        println("加载读取数据库数据中....")
        println("结束读取数据库数据中....")
        return "database data load success ok."
    }

}

class KtBase80 {

//    init {
//        number = number.times(9)
//    }
//
//    var number = 9

    val info: String

    init {
        getInfoMethod()
         info = "DerryOK"
    }

    fun getInfoMethod() {
        println("info:${info[0]}")
    }
}

class KtBase82 (_info: String) {
//    private val info = _info

    val content : String = getInfoMethod()

     private val info = _info // 把这种 转换info的代码，写到最前面，这样保证，就不会出现这种问题

    private fun getInfoMethod() = info // 当此函数调用info变量的时候，你以为是赋值好了，但是还没有赋值
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


    println()

    // TODO 78.Kotlin语言的延迟初始化lateinit学习
// 1.lateinit responseResultInfo 定义
// 2.request 懒加载
// 3.showResponseResult
// 4.main 先请求 在显示
    val p = KtBase78()

// 使用他之前，加载一下（用到它才加载，就属于，懒加载）
    p.loadRequest()

// 使用他
    p.showResponseResult()



    // TODO 79.Kotlin语言的惰性初始化by lazy学习
// 1.不使用惰性初始化 databaseData1 = readSQLServerDatabaseAction()
// 2.使用惰性初始化  databaseData2 by lazy
// 3.KtBase82()  睡眠  db1.databaseData1

// lateinit 是在使用的时候，手动加载的懒加载方式，然后再使用
// 惰性初始化by lazy  是在使用的时候，自动加载的懒加载方式，然后再使用
// >>>>>>>>>>>>>>>>>>> 下面是 不使用惰性初始化 by lazy  普通方式(饿汉式 没有任何懒加载的特点)
    /*val p = KtBase79()

    Thread.sleep(5000)

    println("即将开始使用")

    println("最终显示:${p.databaseData1}")*/


// >>>>>>>>>>>>>>>>>>> 使用惰性初始化 by lazy  普通方式
    val p3 = KtBase79()

    Thread.sleep(5000)

    println("即将开始使用")

    println("最终显示:${p3.databaseData2}")

//    KtBase80();

    println("内容的长度是:${KtBase82("Derry").content.length}")
}