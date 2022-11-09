package com.demo.kotlinapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 变量的声明方式
//        var maxiumAge : Int = 5;
        var maxiumAge = 5;

        Log.v(TAG, "maxiumAge = " + maxiumAge + "; type = " + maxiumAge::class.simpleName)

        val value = 5;      // 声明一个只读变量
        var var1 = 6;       // 声明一个可变变量
        var1 += 1;

        var long : Long = 324;

        val school = "0学";
        val level : Any = when(school) {
            "学前班" -> "幼儿"
            "小学" -> "少儿"
            "中学" -> "青少年"
            "大学" -> "成年"
            else -> {
                println("未知")
            }
        }
        System.out.println("level = " + level)
        Log.v(TAG, "level = " + level)

        val origin = "Jack"
        val dest = "Rose";
        println("$origin love $dest!")

        val flag = false
        println("Answer is : ${if (flag) "我可以" else "对不起"}")
    }

    companion object Static {
        var TAG : String = "noahedu.MainActivity";
    }
}