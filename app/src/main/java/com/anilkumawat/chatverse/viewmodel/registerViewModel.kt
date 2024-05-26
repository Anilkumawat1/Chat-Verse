package com.anilkumawat.chatverse.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anilkumawat.chatverse.model.registerAccountModel
import com.anilkumawat.chatverse.model.apiResponseModel
import com.anilkumawat.chatverse.repository.registerRepository

import com.anilkumawat.mvvmloginsignup.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class registerViewModel(val registerRepository : registerRepository, val application: Application) : ViewModel() {
    val registerAccount : MutableLiveData<Resource<apiResponseModel>> = MutableLiveData()

    fun register(registerAccountdata: registerAccountModel) = viewModelScope.launch {
        registerAccount.postValue(Resource.Loading())
        val response = registerRepository.registerAccount(registerAccountdata)
        registerAccount.postValue(handleResponse(response))
    }

    private fun handleResponse(response: Response<apiResponseModel>) : Resource<apiResponseModel>{
        if(response.isSuccessful){
            response.body()?.let{ Response ->
                return Resource.Success(Response)
            }
        }
        return Resource.Error(response.message())
    }
}