package com.derry.livedatabusandviewbinding.simple1

import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * 单例模式 去掉黏性事件（有关闭黏性的开关） KT的版本
 */
object OKLiveDataBusKT {

    // 存放订阅者
    private val bus : MutableMap<String, BusMutableLiveData<Any>> by lazy { HashMap() }

    // 暴露一个函数，给外界注册 订阅者关系
    @Synchronized
    fun <T> with(key: String, type: Class<T>, isStick: Boolean = true) : BusMutableLiveData<T> {
        if (!bus.containsKey(key)) {
            bus[key] = BusMutableLiveData(isStick)
        }
        return bus[key] as BusMutableLiveData<T>
    }

    // Any? 是 Object ,  * 星投影 KT泛型的？  有点像 Java ?
    class BusMutableLiveData<T> private constructor() : MutableLiveData<T>() {

        var isStick: Boolean = false

        // 次构造
        constructor(isStick: Boolean) : this() {
            this.isStick = isStick
        }

        // 我是先执行
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer) // 这句会执行父类的  // 启用系统的功能  不写就破坏了

            if (!isStick) {
                hook(observer = observer)
                Log.d("derry", "Kotlin版本的 不启用黏性")
            } else {
                Log.d("derry", "Kotlin版本的 启用黏性")
            }
        }

        private fun hook(observer: Observer<in T>) {
            // TODO 1.得到mLastVersion
            // 获取到LivData的类中的mObservers对象
            val liveDataClass = LiveData::class.java

            val mObserversField: Field = liveDataClass.getDeclaredField("mObservers")
            mObserversField.isAccessible = true // 私有修饰也可以访问

            // 获取到这个成员变量的对象  Any == Object
            val mObserversObject: Any = mObserversField.get(this)

            // 得到map对象的class对象   private SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers =
            val mObserversClass: Class<*> = mObserversObject.javaClass

            // 获取到mObservers对象的get方法   protected Entry<K, V> get(K k) {
            val get: Method = mObserversClass.getDeclaredMethod("get", Any::class.java)
            get.isAccessible = true // 私有修饰也可以访问

            // 执行get方法
            val invokeEntry: Any = get.invoke(mObserversObject, observer)

            // 取到entry中的value   is "AAA" is String    is是判断类型 as是转换类型
            var observerWraper: Any? = null
            if (invokeEntry != null && invokeEntry is Map.Entry<*, *>) {
                observerWraper = invokeEntry.value
            }
            if (observerWraper == null) {
                throw NullPointerException("observerWraper is null")
            }

            // 得到observerWraperr的类对象
            val supperClass: Class<*> = observerWraper.javaClass.superclass
            val mLastVersion: Field = supperClass.getDeclaredField("mLastVersion")
            mLastVersion.isAccessible = true

            // TODO 2.得到mVersion
            val mVersion: Field = liveDataClass.getDeclaredField("mVersion")
            mVersion.isAccessible = true

            // TODO 3.mLastVersion=mVersion
            val mVersionValue: Any = mVersion.get(this)
            mLastVersion.set(observerWraper, mVersionValue)
        }
    }
}