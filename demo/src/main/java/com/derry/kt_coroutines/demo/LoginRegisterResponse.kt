package com.derry.kt_coroutines.demo

data class LoginRegisterResponse(val admin: Boolean,
                val chapterTops: List<String>?,
                val coinCount: Int,
                val collectIds: List<Int>,
                val email: String?,
                val icon: String?,
                val id: Int,
                val nickname: String?,
                val password: String?,
                val publicName: String?,
                val token: String?,
                val type: Int,
                val username: String?)