package com.demo.stage1

// 主构造函数：规范来说，都是增加_xxx的方式，临时的输入类型，不能直接用，需要接收下来 成为变量才能用
// _name 等等，都是临时的类型，不能直接要弄，需要转化一下才能用
class KtBase72(_name: String, _sex: Char, _age: Int, _info: String) // 主构造函数
{
    var name = _name
        get() = field // get不允许私有化
        private set(value) {
            field = value
        }

    val sex = _sex
        get() = field
    // set(value) {} 只读的，不能修改的，不能set函数定义

    val age: Int = _age
        get() = field + 1

    val info = _info
        get() = "【${field}】"

    fun show() {
        // println(_name) 临时的输入类型，不能直接用，需要接收下来 成为变量才能用
        println(name)
        println(sex)
        println(age)
        println(info)
    }
}

// TODO 72.Kotlin语言的主构造函数学习
fun main() {
    // KtBase72()

    val p = KtBase72(_name = "Zhangsan", _info = "学习KT语言", _age = 88, _sex = 'M')
    // println(p.name)
    // p.name = "AAA" 被私有化了，不能调用

    p.show()


    // 背后隐式代码：System.out.println(new KtBase71().getNumber());
    println(KtBase71().number)

    // 背后隐式代码：new KtBase71().setNumber(9);
    // KtBase71().number = 9 // val 根本就没有 setXXX函数，只有 getXXX函数

    // 背后隐式代码：System.out.println(new KtBase71().getNumber2());
    println(KtBase71().number2)

    // 背后隐式代码：System.out.println(new KtBase71().getShowInfo());
    println(KtBase71().getShowInfo())


    val p2 = KtBase74("李元霸") // 调用主构造

    KtBase74("张三", '男') // 调用 2个参数的次构造函数

    KtBase74("张三2", '男', 88) // 调用 3个参数的次构造函数

    KtBase74("张三3", '男', 78, "还在学校新语言") // 调用 4个参数的次构造函数



    val p3 = KtBase75("李元霸2") // 调用主构造

    KtBase75("张三", '男') // 调用 2个参数的次构造函数

    KtBase75("张三2", '男', 88) // 调用 3个参数的次构造函数

    KtBase75("张三3", '男', 78, "还在学校新语言") // 调用 4个参数的次构造函数

    KtBase75() // 到底是调用哪一个 构造函数，是次构造 还是 主构造 ？ 答：优先调用主构造函数
}

class KtBase71 {
    val number : Int = 0
    /* 背后的代码：
       private int number = 0;

       public int getNumber() {
            return this.number;
       }
     */

    // 计算属性  下面这样写 get函数覆盖了 field 内容本身，相当于field失效了，无用了，以后用不到了
    val number2 : Int
        get() = (1..1000).shuffled().first() // 从1到1000取出随机值 返回给 getNumber2()函数
    /*
        背后隐式代码：
        为什么没有看到 number2 属性定义？
        答：因为属于 计算属性 的功能，根本在getNumber2函数里面，就没有用到 number2属性，所以 number2属性 失效了，无用了，以后用不到了
         public int getNumber2() {
            return (1..1000).shuffled().first()java的随机逻辑 复杂 ;
       }
     */

    var info: String ? = null // ""

    // 防范竞态条件  当你调用成员，这个成员，可能为null，可能为空值，就必须采用 防范竞态条件，这个是KT编程的规范化
    fun getShowInfo() : String {

        // 这个成员，可能为null，可能为空值，就启用 防范竞态条件
        // 这种写法，就属于 防范竞态条件，我们可以看到专业的KT开发者，有大量这种代码
        // also永远都是返回 info本身
        return info?.let {
            if (it.isBlank()) {
                "info你原来是空值，请检查代码..." // 是根据匿名函数最后一行的变化而变化
            } else {
                "最终info结果是:$it" // 是根据匿名函数最后一行的变化而变化
            }
        } ?: "info你原来是null，请检查代码..."
    }
}

class KtBase74(name: String) // 主构造
{
    // 2个参数的次构造函数，必须要调用主构造函数，否则不通过，  为什么次构造必须调用主构造？答：主构造统一管理 为了更好的初始化设计
    constructor(name: String, sex: Char) : this(name) {
        println("2个参数的次构造函数 name:$name, sex:$sex")
    }

    // 3个参数的次构造函数，必须要调用主构造函数
    constructor(name: String, sex: Char, age: Int) : this(name) {
        println("3个参数的次构造函数 name:$name, sex:$sex, age:$age")
    }

    // 4个参数的次构造函数，必须要调用主构造函数
    constructor(name: String, sex: Char, age: Int, info: String) : this(name) {
        println("4个参数的次构造函数 name:$name, sex:$sex, age:$age, info:$info")
    }
}

class KtBase75(name: String = "李元霸") // 主构造
{
    // 2个参数的次构造函数，必须要调用主构造函数
    constructor(name: String = "李连杰", sex: Char = 'M') : this(name) {
        println("2个参数的次构造函数 name:$name, sex:$sex")
    }

    // 3个参数的次构造函数，必须要调用主构造函数
    constructor(name: String = "李小龙", sex: Char = 'M', age: Int = 33) : this(name) {
        println("3个参数的次构造函数 name:$name, sex:$sex, age:$age")
    }

    // 4个参数的次构造函数，必须要调用主构造函数
    constructor(name: String = "李俊", sex: Char = 'W', age: Int = 87, info: String = "还在学校新开发语言") : this(name) {
        println("4个参数的次构造函数 name:$name, sex:$sex, age:$age, info:$info")
    }
}