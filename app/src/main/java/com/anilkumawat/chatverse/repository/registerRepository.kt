package com.anilkumawat.chatverse.repository

import com.anilkumawat.chatverse.api.RetrofitInstance
import com.anilkumawat.chatverse.model.registerAccountModel


class registerRepository {
    suspend fun registerAccount(registerAccount: registerAccountModel) = RetrofitInstance.api.registerAccount(registerAccount)
}