package com.anilkumawat.chatverse.model

data class User(
    val _id: String,
    val authToken: String,
    val email: String,
    val name: String
)