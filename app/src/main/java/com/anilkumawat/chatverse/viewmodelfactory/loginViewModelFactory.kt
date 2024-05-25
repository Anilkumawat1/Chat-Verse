package com.anilkumawat.chatverse.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anilkumawat.chatverse.repository.loginRepository
import com.anilkumawat.chatverse.viewmodel.loginViewModel


class loginViewModelFactory(val loginRepository: loginRepository, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(loginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return loginViewModel(loginRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}