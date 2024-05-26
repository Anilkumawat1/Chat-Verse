package com.anilkumawat.chatverse.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anilkumawat.chatverse.model.apiResponseModel
import com.anilkumawat.chatverse.model.emailModel
import com.anilkumawat.chatverse.model.tokenModel
import com.anilkumawat.chatverse.repository.splashScreenRepository
import com.anilkumawat.mvvmloginsignup.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class splashScreenViewModel(val splashScreenRepository: splashScreenRepository,val application: Application) : ViewModel() {
    private val _validToken : MutableLiveData<Resource<apiResponseModel>> = MutableLiveData()
    val validToken : LiveData<Resource<apiResponseModel>> get() = _validToken

    fun checkAuth (tokenModel: tokenModel) = viewModelScope.launch {
        _validToken.postValue(Resource.Loading())
        val response = splashScreenRepository.checkAuth(tokenModel)
        _validToken.postValue(handelAuthResponse(response))
    }

    fun handelAuthResponse(response : Response<apiResponseModel>): Resource<apiResponseModel>{
        if(response.isSuccessful){
            response.body()?.let{ Response ->
                return Resource.Success(Response)
            }
        }
        return Resource.Error(response.message())
    }
}