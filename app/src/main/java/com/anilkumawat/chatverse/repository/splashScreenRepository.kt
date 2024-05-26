package com.anilkumawat.chatverse.repository

import com.anilkumawat.chatverse.api.RetrofitInstance
import com.anilkumawat.chatverse.model.emailModel
import com.anilkumawat.chatverse.model.tokenModel

class splashScreenRepository {
    suspend fun checkAuth(tokenModel: tokenModel) = RetrofitInstance.api.checkAuth(tokenModel)
}