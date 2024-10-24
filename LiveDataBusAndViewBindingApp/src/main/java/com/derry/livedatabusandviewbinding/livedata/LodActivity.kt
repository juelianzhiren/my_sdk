package com.derry.livedatabusandviewbinding.livedata

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MyLiveData.data1.value = "我是黏性数据，我是旧数据"

        startActivity(Intent(this, DerryActivity::class.java))
    }
}