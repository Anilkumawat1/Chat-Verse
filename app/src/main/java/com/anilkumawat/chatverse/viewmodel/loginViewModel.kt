package com.anilkumawat.chatverse.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.anilkumawat.chatverse.model.loginModel
import com.anilkumawat.chatverse.model.apiResponseModel
import com.anilkumawat.chatverse.repository.loginRepository
import com.anilkumawat.mvvmloginsignup.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class loginViewModel(val loginRepository: loginRepository,val application: Application) : ViewModel() {
    private val _loginResponse : MutableLiveData<Resource<apiResponseModel>> = MutableLiveData()
    val loginResponse : LiveData<Resource<apiResponseModel>> get() = _loginResponse

    fun login(loginModel: loginModel) = viewModelScope.launch{
        _loginResponse.postValue(Resource.Loading())
        val response = loginRepository.loginAccount(loginModel)
        _loginResponse.postValue(handelloginResponse(response))

    }
    fun handelloginResponse(response: Response<apiResponseModel>) : Resource<apiResponseModel> {
        if(response.isSuccessful){
            response.body()?.let{ Response ->
                return Resource.Success(Response)
            }
        }
        return Resource.Error(response.message())
    }
}