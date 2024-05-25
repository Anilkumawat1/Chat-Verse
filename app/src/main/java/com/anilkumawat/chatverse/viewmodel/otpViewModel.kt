package com.anilkumawat.chatverse.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anilkumawat.chatverse.model.registerResponseModel
import com.anilkumawat.chatverse.model.resendOtpModel
import com.anilkumawat.chatverse.model.validateOtpModel
import com.anilkumawat.chatverse.repository.otpRepository

import com.anilkumawat.mvvmloginsignup.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class otpViewModel(val otpRepository: otpRepository, val application: Application) : ViewModel() {
    private val _validateOtp = MutableLiveData<Resource<registerResponseModel>>()
    val validateOtp: LiveData<Resource<registerResponseModel>> get() = _validateOtp

    private val _timeRemaining = MutableLiveData<Long>()
    val timeRemaining: LiveData<Long> get() = _timeRemaining

    private val _resendOtp = MutableLiveData<Resource<registerResponseModel>>()
    val resendOtp: LiveData<Resource<registerResponseModel>> get() = _resendOtp

    private var countDownTimer: CountDownTimer? = null
    init {
        startTimer()
    }
    fun startTimer() {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeRemaining.value = millisUntilFinished / 1000
            }

            override fun onFinish() {
                _timeRemaining.value = 0
            }
        }.start()
    }

    fun validate(validateOtp: validateOtpModel) = viewModelScope.launch {
        _validateOtp.postValue(Resource.Loading())
        val response = otpRepository.validateOtp(validateOtp)
        _validateOtp.postValue(handleResponse(response))
    }

    private fun handleResponse(response: Response<registerResponseModel>) : Resource<registerResponseModel> {
        if(response.isSuccessful){
            response.body()?.let{ Response ->
                return Resource.Success(Response)
            }
        }
        return Resource.Error(response.message())
    }

    fun resend(resendOtp: resendOtpModel) = viewModelScope.launch {
        _resendOtp.postValue(Resource.Loading())
        val response = otpRepository.resendOtp(resendOtp)
        _resendOtp.postValue(handelResendResponse(response))
    }

    private fun handelResendResponse(response: Response<registerResponseModel>) : Resource<registerResponseModel>{
        if(response.isSuccessful){
            response.body()?.let {Response->
                return Resource.Success(Response)
            }
        }
        return Resource.Error(response.message())
    }

}