package com.ivanere.models

data class User(
    val id: Int? = null,
    val fio: String,
    val hash_pw: String,
    val role: Int
)