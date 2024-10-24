package com.derry.livedatabusandviewbinding.simple1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.derry.livedatabusandviewbinding.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 如果LiveData默认的黏性，会产生Bug，你就需要剔除黏性

        // livedata发消息通知所有的观察者数据变化了
        OKLiveDataBusKT.with("data1", String::class.java).value = "九阳神功" // Kotlin版本  九阳神功 旧数据  黏性数据
        OKLiveDataBusJava.getInstance().with("data2", String::class.java).value= "Nine Suns" // Java版本

        // 点击事件，跳转下一个Activity
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            startActivity(Intent(this, MainActivity2::class.java))
        }
    }
}