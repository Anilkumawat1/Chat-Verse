package com.anilkumawat.chatverse.api


import com.anilkumawat.chatverse.model.loginModel
import com.anilkumawat.chatverse.model.registerAccountModel
import com.anilkumawat.chatverse.model.apiResponseModel
import com.anilkumawat.chatverse.model.emailModel
import com.anilkumawat.chatverse.model.tokenModel
import com.anilkumawat.chatverse.model.validateOtpModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RestApi {

    @POST("register")
    suspend fun registerAccount(@Body registerAccount: registerAccountModel) : Response<apiResponseModel>

    @POST("validate")
    suspend fun validateOtp(@Body validateOtp: validateOtpModel) : Response<apiResponseModel>

    @POST("resend")
    suspend fun resendOtp(@Body resendOtp: emailModel) : Response<apiResponseModel>

    @POST("login")
    suspend fun loginAccount(@Body loginModel: loginModel) : Response<apiResponseModel>

    @POST("checkAuth")
    suspend fun checkAuth(@Body tokenModel: tokenModel) : Response<apiResponseModel>

}