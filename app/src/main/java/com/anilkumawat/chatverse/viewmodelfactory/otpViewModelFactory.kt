package com.anilkumawat.chatverse.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anilkumawat.chatverse.repository.otpRepository
import com.anilkumawat.chatverse.viewmodel.otpViewModel


class otpViewModelFactory(val otpRepository: otpRepository, val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(otpViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return otpViewModel(otpRepository, application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
