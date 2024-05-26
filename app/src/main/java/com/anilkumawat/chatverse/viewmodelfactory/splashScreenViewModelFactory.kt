package com.anilkumawat.chatverse.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anilkumawat.chatverse.repository.otpRepository
import com.anilkumawat.chatverse.repository.splashScreenRepository
import com.anilkumawat.chatverse.viewmodel.otpViewModel
import com.anilkumawat.chatverse.viewmodel.splashScreenViewModel

class splashScreenViewModelFactory(val splashScreenRepository: splashScreenRepository, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(splashScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return splashScreenViewModel(splashScreenRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
