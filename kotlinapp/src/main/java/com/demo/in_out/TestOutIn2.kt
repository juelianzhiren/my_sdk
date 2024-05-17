package k06.generic.simple3

import com.demo.in_out.obj.FuClass
import com.demo.in_out.obj.ZiClass

val fuClass = FuClass()
val ziClass = ZiClass()

fun main() {
    // ? extends   ==   out  生产者
    val list: MutableList<out FuClass> = ArrayList<ZiClass>();

    // ? super ==  in  消费者
    val list2: MutableList<in ZiClass> = ArrayList<FuClass>();
}

/*
泛型相当于奶茶店，
消费者（客户）  只能修改   给钱决定买什么口味的（给钱决定奶茶口味），
生产者（店员）  只能获取   收钱办事，不能自作主张给什么奇怪口味的奶茶（收钱给奶茶），
 */