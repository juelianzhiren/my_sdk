package com.demo.kt_coroutines.use.ui

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.demo.kotlinapp.R
import com.demo.kt_coroutines.use.api.APIClient
import com.demo.kt_coroutines.use.api.WanAndroidAPI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

// 2.2：协程方式完成异步任务网络加载
class MainActivity2 : AppCompatActivity() {

    val main = MainScope()

    private val TAG = "Derry"
    var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // 按钮 点击事件
    fun startRequest(view: View) {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.setTitle("请求服务器中...")
        mProgressDialog?.show()

        // GlobalScope 全局作用域，一般不用全局的，触发测试的时候，可以用

        // Android中使用的话，他默认是IO线程，所以需要修改成main线程
        main.launch(Dispatchers.Main) {
            // UI 线程
            val result =

                APIClient.instance.instanceRetrofit(WanAndroidAPI::class.java)
                    .loginActionCoroutine("Derry-vip", "123456") // 1.挂起出去执行异步操作  2.操作完成后恢复主线程

            // 更新UI 因为这个括号里面是主线程，当然可以更新UI
            Log.d(TAG, "errorMsg: ${result.data}")
            textView.text = result.data.toString() // 更新控件 UI

            mProgressDialog?.dismiss() // 隐藏加载框
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        main.cancel()
    }
}