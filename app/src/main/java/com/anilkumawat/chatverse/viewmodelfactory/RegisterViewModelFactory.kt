package com.anilkumawat.chatverse.viewmodelfactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anilkumawat.chatverse.repository.registerRepository
import com.anilkumawat.chatverse.viewmodel.registerViewModel


class registerViewModelFactory(val registerRepository : registerRepository, val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(registerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return registerViewModel(registerRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}