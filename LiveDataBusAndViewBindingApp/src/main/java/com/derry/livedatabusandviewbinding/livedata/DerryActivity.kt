package com.derry.livedatabusandviewbinding.livedata

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.derry.livedatabusandviewbinding.livedata.MyLiveData
import kotlin.concurrent.thread

class DerryActivity : AppCompatActivity() {
    private val TAG = "DerryActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ①：订阅观察者 眼睛
        MyLiveData.data1.observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                // 更新UI
                Log.v(TAG, "onChanged, t = ${t}")
            }
        })

        // ②：触发数据改变
        thread {
            Thread.sleep(3000)
            MyLiveData.data1.postValue("三秒钟后，数据触发改变了")
        }
    }
}