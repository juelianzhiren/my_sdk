package com.demo.kt_coroutines.demo

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.demo.kotlinapp.R

class MainActivity : AppCompatActivity() {
    private var usernameEt: EditText? = null
    private var passwordEt: EditText? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_main_demo)

        usernameEt = findViewById<EditText>(R.id.username_et);
        passwordEt = findViewById<EditText>(R.id.password_et);

        findViewById<Button>(R.id.register_btn).setOnClickListener { register() }

        findViewById<Button>(R.id.login_btn).setOnClickListener { login() }
    }

    private fun register() {
        val username = usernameEt!!.text;
        val password = passwordEt!!.text;
        if (username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_LONG).show()
            return
        }
    }

    private fun login() {
        val username = usernameEt!!.text;
        val password = passwordEt!!.text;
        if (username.isBlank() || password.isBlank()) {
            Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_LONG).show()
            return
        }

    }

    private fun String.isEmpty() : Boolean {
        return this == null || this.length == 0;
    }
}

