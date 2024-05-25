package com.anilkumawat.chatverse.api


import com.anilkumawat.chatverse.model.loginModel
import com.anilkumawat.chatverse.model.registerAccountModel
import com.anilkumawat.chatverse.model.registerResponseModel
import com.anilkumawat.chatverse.model.resendOtpModel
import com.anilkumawat.chatverse.model.validateOtpModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RestApi {

    @POST("register")
    suspend fun registerAccount(@Body registerAccount: registerAccountModel) : Response<registerResponseModel>

    @POST("validate")
    suspend fun validateOtp(@Body validateOtp: validateOtpModel) : Response<registerResponseModel>

    @POST("resend")
    suspend fun resendOtp(@Body resendOtp: resendOtpModel) : Response<registerResponseModel>

    @POST("login")
    suspend fun loginAccount(@Body loginModel: loginModel) : Response<registerResponseModel>

}