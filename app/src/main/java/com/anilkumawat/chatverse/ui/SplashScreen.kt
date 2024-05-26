package com.anilkumawat.chatverse.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anilkumawat.chatverse.R
import com.anilkumawat.chatverse.databinding.ActivitySplashScreenBinding
import com.anilkumawat.chatverse.model.emailModel
import com.anilkumawat.chatverse.model.tokenModel
import com.anilkumawat.chatverse.repository.splashScreenRepository
import com.anilkumawat.chatverse.utils.SessionManager
import com.anilkumawat.chatverse.viewmodel.registerViewModel
import com.anilkumawat.chatverse.viewmodel.splashScreenViewModel
import com.anilkumawat.chatverse.viewmodelfactory.registerViewModelFactory
import com.anilkumawat.chatverse.viewmodelfactory.splashScreenViewModelFactory
import com.anilkumawat.mvvmloginsignup.utils.Resource
import com.google.android.material.snackbar.Snackbar


class SplashScreen : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 3000
    private lateinit var mBinding : ActivitySplashScreenBinding
    private lateinit var viewModel : splashScreenViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashScreenBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

         val splashScreenRepository = splashScreenRepository()
         val viewModelProviderFactory = splashScreenViewModelFactory(splashScreenRepository,application)
         viewModel = ViewModelProvider(this,viewModelProviderFactory).get(splashScreenViewModel::class.java)


        Handler().postDelayed({
            val authToken = SessionManager.getToken(this)
            if(authToken==null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val tokenModel = tokenModel(authToken)
                viewModel.checkAuth(tokenModel)
            }

            viewModel.validToken.observe(this, Observer {response->
                when(response){
                    is Resource.Loading->{

                    }
                    is Resource.Success->{
                        if(response.data!!.success){
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            SessionManager.clearData(this)
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    is Resource.Error->{
                        showSnackbar("Check your internet")

                    }
                }
            })
        }, SPLASH_TIME_OUT)
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}