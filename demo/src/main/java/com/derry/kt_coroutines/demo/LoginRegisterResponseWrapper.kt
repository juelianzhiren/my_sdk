package com.derry.kt_coroutines.demo

data class LoginRegisterResponseWrapper<T>(val data: T?, val errorCode: Int, val errorMsg: String?)