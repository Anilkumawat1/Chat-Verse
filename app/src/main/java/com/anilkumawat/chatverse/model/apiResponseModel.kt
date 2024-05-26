package com.anilkumawat.chatverse.model

data class apiResponseModel(
    val data: Any,
    val message: List<String>,
    val success: Boolean
)