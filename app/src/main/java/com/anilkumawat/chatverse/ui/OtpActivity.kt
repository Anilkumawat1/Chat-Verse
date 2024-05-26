package com.anilkumawat.chatverse.ui

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anilkumawat.chatverse.R
import com.anilkumawat.chatverse.databinding.ActivityOtpBinding
import com.anilkumawat.chatverse.model.emailModel
import com.anilkumawat.chatverse.model.validateOtpModel
import com.anilkumawat.chatverse.repository.otpRepository
import com.anilkumawat.chatverse.viewmodel.otpViewModel
import com.anilkumawat.chatverse.viewmodelfactory.otpViewModelFactory
import com.anilkumawat.mvvmloginsignup.utils.Resource
import com.google.android.material.snackbar.Snackbar

class OtpActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityOtpBinding
    private lateinit var viewModel: otpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mBinding = ActivityOtpBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        val email = intent.getStringExtra("user_email")
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        showKeyboard(mBinding.pinview)
        val otpRepository = otpRepository()
        val viewModelProviderFactory = otpViewModelFactory(otpRepository,application)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(otpViewModel::class.java)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        viewModel.validateOtp.observe(this, Observer { response->
            when(response){
                is Resource.Success->{
                    alertDialog.dismiss()

                    val data = response.data!!.success
                    if(data){
                        navigateToSecondActivity()
                    }
                    else{
                        showSnackbar(response.data.message[0])
                    }
                }
                is Resource.Error -> {
                    alertDialog.dismiss()
                    response.message?.let{
                            message-> Log.e(ContentValues.TAG, "An error occured: $message")
                    }
                }
                is Resource.Loading ->{

                    alertDialog.show()

                }
            }

        })
        viewModel.timeRemaining.observe(this, Observer { time ->
            if (time == 0L) {
                mBinding.resendOtp.text = "Resend otp"
            } else {
                mBinding.resendOtp.text = "Seconds remaining: ${time}"
            }
        })
        viewModel.resendOtp.observe(this,Observer{ response->

            when(response){
                is Resource.Success ->{
                    alertDialog.dismiss()
                    viewModel.startTimer()
                    val data = response.data!!.success
                    if(data){
                        showSnackbar(response.data.message[0])
                    }
                    else{
                        showSnackbar(response.data.message[0])
                    }
                }
                is Resource.Error ->{
                    alertDialog.dismiss()
                }
                is Resource.Loading->{
                    alertDialog.show()
                }
            }

        })

        mBinding.resendOtp.setOnClickListener {
            timmerForResend(email!!)
        }

        mBinding.otpverifiybtn.setOnClickListener {
            if(validateOtpEnter()){
                val validateOtp = validateOtpModel(email!!,mBinding.pinview.text.toString())
                viewModel.validate(validateOtp)
            }
        }
    }
    private fun timmerForResend(email : String){
        if(mBinding.resendOtp.text.toString()=="Resend otp"){
            val resendOtp = emailModel(email)
            viewModel.resend(resendOtp)
        }
    }
    private fun navigateToSecondActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
    private fun validateOtpEnter():Boolean{
        var error : String? = null
        if(mBinding.pinview.text.toString().length!=6){
            error = "Please enter otp"
            Toast.makeText(this,error, Toast.LENGTH_SHORT).show()
        }
        return error==null
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }
    private fun showKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}