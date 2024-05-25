package com.anilkumawat.chatverse.repository

import com.anilkumawat.chatverse.api.RetrofitInstance
import com.anilkumawat.chatverse.model.loginModel

class loginRepository {
    suspend fun loginAccount(loginModel: loginModel) = RetrofitInstance.api.loginAccount(loginModel)
}