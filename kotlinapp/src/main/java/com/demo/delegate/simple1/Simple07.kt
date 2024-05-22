package com.demo.delegate.simple1

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Owner {

    // var （自定义委托中，必须有 set get）
    // String （自定义委托中，get返回String，set传入String）
    // get/set方法 第一个参数 必须包含 本类Owner或Owner父类
    var text: String by Simple07()

    var text2 : String by StringDelegate()
}

class Simple07 {

    private var str: String = "Default"

    operator fun getValue(owner: Owner, property: KProperty<*>) : String {
        println("getValue执行啦")
        return str
    }

    operator fun setValue(owner: Owner, property: KProperty<*>, value : String) {
        println("setValue执行啦")
        str = value
    }
}

class StringDelegate() : ReadWriteProperty<Owner, String> {

    private var str: String = "Default"

    override fun getValue(thisRef: Owner, property: KProperty<*>): String {
        println("ReadWriteProperty getValue执行啦")
        return str
    }

    override fun setValue(thisRef: Owner, property: KProperty<*>, value: String) {
        println("ReadWriteProperty setValue执行啦")
        str = value
    }

}

fun main() {
    val o = Owner()
    o.text = "DERRY"
    println(o.text)

    o.text2 = "AAAA"
    println(o.text2)
}