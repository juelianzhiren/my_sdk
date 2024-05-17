package com.demo.in_out

interface Producer<out T> { // 生产者 out
    fun produce(): T // 只能生产

    // 编译不通过，不能消费
    /*fun consumer(item : T)
    fun consumer2(item : T)
    fun consumer3(item : T)
    fun consumer4(item : T)*/
    // ...
}

interface Consumer<in T> { // 消费者 in
    /*fun producer() : T // 不能能生产
    fun producer2() : T // 不能能生产
    fun producer3() : T // 不能能生产
    fun producer4() : T // 不能能生产
    fun producer5() : T // 不能能生产*/
    // ...

    // 只能消费
    fun consumer(item: T)
}

// TODO >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> 下面是实际代码

// Producer 生产者 出去就是生产者  读取
class MyStudentGet<out T>(_item: T) {
    private val item = _item
    fun get(): T = item
}

// Consumer 消费者  进去就是消费者 修改
class MyStudentSet<in T>() {
    fun set(value: T) = println("你传递进来的内容是:$value")
}

// Producer 合并 Consumer
class StudentSetGet<in I, out O> {

    private var item: I? = null

    // 消费者
    fun set(value: I) {
        println("你传递进来的内容是:$value")
        item = value
    }

    // 生产者  as xx 转成xx类型  强制转成生产者，才能返回给外界
    fun get() = item as? O
}

// 我们的普通代码，就是不变
// 不变
class StudentSetGets<INPUT_OUTPU> {

    private var item: INPUT_OUTPU? = null

    // 消费者
    fun set(value: INPUT_OUTPU) {
        println("你传递进来的内容是:$value")
        item = value
    }

    // 生产者
    fun get() = item
}

fun main() {
    // 生产者 协变  ? extends T  或者 out T
    val stu = MyStudentGet("Derry is OK")
    println(stu.get())

    // 消费者 逆变 ? super T 或者 in T
    val stu2 = MyStudentSet<Double>()
    stu2.set(989889.4)

    // 消费者 逆变 ? super T 或者 in T        +        生产者 协变  ? extends T  或者 out T
    val stu3 = StudentSetGet<String, String>()
    stu3.set("九阳神功") // 消费者
    println(stu3.get()) // 生产者
}