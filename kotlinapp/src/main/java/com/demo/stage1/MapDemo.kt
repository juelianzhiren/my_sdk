package com.demo.stage1

import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.N)
fun main() {
    val mMap1 : Map<String, Double> = mapOf<String, Double>("Derry" to(534.4), "Kevin" to 454.5)
    val mMap2 = mapOf(Pair("Derry", 545.4), Pair("Kevin", 664.4))
    // 上面两种方式是等价的哦


    val mMap /*: Map<String, Int>*/ = mapOf("Derry" to 123,"Kevin" to 654)

    // 方式一 [] 找不到会返回null
    println(mMap["Derry"]) // 背后对[] 运算符重载了
    println(mMap["Kevin"])
    println(mMap.get("Kevin")) // get 与 [] 完完全全等价的
    println(mMap["XXX"]) // map通过key找 如果找不到返回null，不会奔溃

    println()

    // 方式二 getOrDefault
    println(mMap.getOrDefault("Derry", -1))
    println(mMap.getOrDefault("Derry2", -1))

    // 方式三 getOrElse
    println(mMap.getOrElse("Derry") {-1})
    println(mMap.getOrElse("Derry2") {-1})

    println()

    // 方式四 getValue 与Java一样 会奔溃  尽量不要使用此方式
    println(mMap.getValue("Derry"))
//    println(mMap.getValue("XXX"))

    // map获取内容，尽量使用 方式二 方式三


    val map /*: Map<String, Int>*/ = mapOf(Pair("Derry", 123), Pair("Kevin", 456), "Leo" to 789)

    // 第一种方式:
    map.forEach {
        // it 内容 每一个元素 (K 和 V)  每一个元素 (K 和 V)  每一个元素 (K 和 V)
        // it 类型  Map.Entry<String, Int>
        println("K:${it.key} V:${it.value}")
    }

    println()

    // 第二种方式：
    map.forEach { key: String, value: Int ->
        // 把默认的it给覆盖了
        println("key:$key, value:$value")
    }

    println()

    // 第三种方式：
    map.forEach { (k /*: String*/, v /*: Int*/) ->
        println("key:$k, value:$v")
    }

    println()

    // 第四种方式：
    for (item /*: Map.Entry<String, Int>*/ in map) {
        // item 内容 每一个元素 (K 和 V)  每一个元素 (K 和 V)  每一个元素 (K 和 V)
        println("key:${item.key} value:${item.value}")
    }


    // 1.可变集合的操作 += [] put
    val map2 : MutableMap<String, Int> = mutableMapOf(Pair("Derry", 123), "Kevin" to 456, Pair("Dee", 789))
    // 下面是可变操作
    map2 += "AAA" to(111)
    map2 += "BBB" to 1234
    map2 -= "Kevin"
    map2["CCC"] = 888
    map2.put("DDD", 999) // put 和 [] 等价的

    // 2.getOrPut 没有有的情况
    // 如果整个map集合里面没有 FFF的key 元素，我就帮你先添加到map集合中去，然后再从map集合中获取
    val r: Int = map2.getOrPut("FFF") { 555 }
    println(r)
    println(map2["FFF"]) // 他已经帮你加入进去了，所以你可以获取

    // 3.getOrPut 有的情况
    val r2 = map2.getOrPut("Derry") {666} // 发现Derry的key是有的，那么就直接获取出来， 相当于666备用值就失效了
    println(r2)
}