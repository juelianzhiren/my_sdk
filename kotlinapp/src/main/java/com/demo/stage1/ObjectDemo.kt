package com.demo.stage1

object KtBase87 {
    /* object 对象类背后做了什么事情
        public static final KtBase87 INSTANCE;

        private KtBase87() {} // 主构造废除一样的效果

        public final void show() {
            String var1 = "我是show函数...";
            ...
            System.out.println(var1);
        }

        // 这个区域是 object 不同点：
        static {
            KtBase87 var0 = new KtBase87();
            INSTANCE = var0;
            String var1 = "KtBase91 init...";
            ...
            System.out.println(var0);
        }
     */

    init {
        println("KtBase91 init...")
    }

    fun show() = println("我是show函数...")
}


interface RunnableKT {
    fun run()
}

open class KtBase88 {
    open fun add(info: String) = println("KtBase88 add:$info")

    open fun del(info: String) = println("KtBase88 del:$info")
}

// 小结：Java接口，有两种方式 1（object : 对象表达式）  2简洁版，
//       KT接口，只有一种方式 1（object : 对象表达式）
// 具名实现  具体名字 == KtBase88Impl
class KtBase88Impl : KtBase88() {

    override fun add(info: String) {
        // super.add(info)
        println("我是具名对象 add:$info")
    }

    override fun del(info: String) {
        // super.del(info)
        println("我是具名对象 del:$info")
    }
}


// TODO 87.Kotlin语言的对象声明学习
fun main() {
    // object KtBase87 既是单例的实例，也是类名
    // 小结：既然是 单例的实例，又是类名，只有一个创建，这就是典型的单例
    println(KtBase87) // 背后代码：println(KtBase87.INSTANCE)
    println(KtBase87) // 背后代码：println(KtBase87.INSTANCE)
    println(KtBase87)
    println(KtBase87)
    println(KtBase87)
    println(KtBase87)

    // 背后代码：KtBase87.INSTANCE.show();
    println(KtBase87.show())


    println()


    // TODO  88.Kotlin语言的对象表达式学习
// 1.add del println
// 2.匿名对象表达式方式
// 3.具名实现方式
// 4.对Java的接口 用对象表达式方式
// 匿名对象 表达式方式
    val p : KtBase88 = object : KtBase88() {
        override fun add(info: String) {
            // super.add(info)
            println("我是匿名对象 add:$info")
        }

        override fun del(info: String) {
            // super.del(info)
            println("我是匿名对象 del:$info")
        }
    }
    p.add("李元霸")
    p.del("李连杰")


// 具名实现方式
    val p2 = KtBase88Impl()
    p2.add("刘一")
    p2.del("刘二")

// 对Java的接口 用   KT[对象表达式方式]  方式一
    val p3 = object : Runnable {
        override fun run() {
            println("Runnable run ...")
        }
    }
    p3.run()

// 对Java的接口 用   Java最简洁的方式 方式二
    val p4 = Runnable {
        println("Runnable run2 ...")
    }
    p4.run()

// 对KT的接口 用   KT[对象表达式方式]  方式一
    object : RunnableKT {
        override fun run() {
            println("RunnableKT 方式一 run ...")
        }
    }.run()

// 对KT的接口
    /*RunnableKT {

    }*/
}