package com.anilkumawat.chatverse.repository

import com.anilkumawat.chatverse.api.RetrofitInstance
import com.anilkumawat.chatverse.model.emailModel
import com.anilkumawat.chatverse.model.validateOtpModel


class otpRepository {
    suspend fun validateOtp(validateOtp: validateOtpModel) = RetrofitInstance.api.validateOtp(validateOtp)

    suspend fun resendOtp(resendOtp: emailModel) = RetrofitInstance.api.resendOtp(resendOtp)
}