package com.demo.delegate.simple1

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// TODO 提供委托（provideDelegate）

class StringDelegateUpdate(var str: String = "Default") : ReadWriteProperty<Owner2, String> {

    override fun getValue(thisRef: Owner2, property: KProperty<*>): String {
        println("ReadWriteProperty getValue执行啦")
        return str
    }

    override fun setValue(thisRef: Owner2, property: KProperty<*>, value: String) {
        println("ReadWriteProperty setValue执行啦")
        str = value
    }

}

class SmartDelegator {

    // 相当于是一个选择器，动态选择 是哪个 StringDelegateUpdate自定义委托
    operator fun provideDelegate(
            thisRef : Owner2,
            property: KProperty<*>) : ReadWriteProperty<Owner2, String> {

        // 逻辑是自己决定的
        return if (property.name.contains("aaa")) {
            StringDelegateUpdate("bbb")
        } else {
            StringDelegateUpdate("aaa")
        }
    }
}

class Owner2 {
    var aaa: String by SmartDelegator()
    var bbb: String by SmartDelegator()
}

fun main() {
    val owner = Owner2()
    println(owner.aaa)
    println(owner.bbb)
}