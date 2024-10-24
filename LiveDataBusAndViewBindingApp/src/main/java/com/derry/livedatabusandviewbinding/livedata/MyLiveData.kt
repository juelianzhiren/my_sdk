package com.derry.livedatabusandviewbinding.livedata

import androidx.lifecycle.MutableLiveData

object MyLiveData {

    // 懒加载：用到才加载
    val data1: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val data2: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    val data10: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

}