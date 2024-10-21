package com.derry.kt_coroutines.demo

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.derry.kt_coroutines.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var usernameEt: EditText? = null
    private var passwordEt: EditText? = null
    private var mProgressDialog: ProgressDialog? = null
    private val TAG = "ztq"
    private var resultTv: TextView? = null

    val mHandler = Handler(Looper.getMainLooper()) {
        // as xx 转换成xx类型

        val result =  it.obj as LoginRegisterResponseWrapper<LoginRegisterResponse>
        Log.d(TAG, "data: ${result.data}, errormsg: ${result.errorMsg}")
        resultTv?.text = if (result.errorCode == 0) result.data.toString() else result.errorMsg // 更新控件 UI
        val flag = if (it.what == 0) "注册" else "登录"

        mProgressDialog?.dismiss() // 隐藏加载框
        if (result.errorCode == 0) {
            Toast.makeText(MainActivity@this, "${flag}成功~", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(MainActivity@this, "${flag}失败：${result.errorMsg}", Toast.LENGTH_LONG).show()
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_demo)

        usernameEt = findViewById<EditText>(R.id.username_et);
        passwordEt = findViewById<EditText>(R.id.password_et);
        resultTv = findViewById(R.id.result_tv)

        findViewById<Button>(R.id.register_btn).setOnClickListener { register() }

        findViewById<Button>(R.id.login_btn).setOnClickListener { login() }
    }

    private fun register() {
        val username = usernameEt!!.text.toString();
        val password = passwordEt!!.text.toString();
        if (username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_LONG).show()
            return
        }
//        startRegisterRequest(username, password)

        startRegisterRequestByCoroutine(username, password)
    }

    private fun login() {
        val username = usernameEt!!.text.toString();
        val password = passwordEt!!.text.toString();
        if (username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_LONG).show()
            return
        }
//        startLoginRequest(username, password)
        startLoginRequestByCoroutine(username, password)
    }

    // 注册事件
    fun startRegisterRequest(username: String, password: String) {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.setTitle("请求服务器中...")
        mProgressDialog?.show()

        // TODO 第一打步骤：异步线程开启 请求服务器
        object: Thread() {
            override fun run() {
                super.run()

                val loginResult = APIClient.instance.instanceRetrofit(WanAndroidAPI::class.java)
                        .registerAction(username, password, password)

                val result : LoginRegisterResponseWrapper<LoginRegisterResponse>? = loginResult.execute().body()

                // 切换android 主线程，更新UI  把最终登录成功的JavaBean（KT数据类） 发送给  Handler
                val msg = mHandler.obtainMessage()
                msg.what = 0
                msg.obj = result
                mHandler.sendMessage(msg)
            }
        }.start()
    }

    // 注册事件
    fun startRegisterRequestByCoroutine(username: String, password: String) {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.setTitle("请求服务器中...")
        mProgressDialog?.show()

        GlobalScope.launch(Dispatchers.IO) {
            val result = APIClient.instance.instanceRetrofit(WanAndroidAPI::class.java).registerActionCoroutine(username, password, password)

            // 切换android 主线程，更新UI  把最终登录成功的JavaBean（KT数据类） 发送给  Handler
            val msg = mHandler.obtainMessage()
            msg.what = 0
            msg.obj = result
            mHandler.sendMessage(msg)
        }
    }

    // 注册事件
    fun startLoginRequest(username: String, password: String) {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.setTitle("请求服务器中...")
        mProgressDialog?.show()

        // TODO 第一打步骤：异步线程开启 请求服务器
        object: Thread() {
            override fun run() {
                super.run()

                val loginResult = APIClient.instance.instanceRetrofit(WanAndroidAPI::class.java)
                        .loginAction(username, password)

                val result : LoginRegisterResponseWrapper<LoginRegisterResponse>? = loginResult.execute().body()

                // 切换android 主线程，更新UI  把最终登录成功的JavaBean（KT数据类） 发送给  Handler
                val msg = mHandler.obtainMessage()
                msg.what = 1;
                msg.obj = result
                mHandler.sendMessage(msg)
            }
        }.start()
    }

    // 注册事件
    fun startLoginRequestByCoroutine(username: String, password: String) {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog?.setTitle("请求服务器中...")
        mProgressDialog?.show()

        GlobalScope.launch(Dispatchers.IO) {
            val result = APIClient.instance.instanceRetrofit(WanAndroidAPI::class.java).loginActionCoroutine(username, password)

            // 切换android 主线程，更新UI  把最终登录成功的JavaBean（KT数据类） 发送给  Handler
            val msg = mHandler.obtainMessage()
            msg.what = 0
            msg.obj = result
            mHandler.sendMessage(msg)
        }
    }
}