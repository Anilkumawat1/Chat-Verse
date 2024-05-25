package com.anilkumawat.chatverse.ui

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anilkumawat.chatverse.R
import com.anilkumawat.chatverse.databinding.ActivitySignUpBinding
import com.anilkumawat.chatverse.model.registerAccountModel
import com.anilkumawat.chatverse.repository.registerRepository
import com.anilkumawat.chatverse.viewmodel.registerViewModel
import com.anilkumawat.chatverse.viewmodelfactory.registerViewModelFactory
import com.anilkumawat.mvvmloginsignup.utils.Resource

class SignUpActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
    private lateinit var mBinding : ActivitySignUpBinding
    lateinit var  viewModel: registerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySignUpBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        mBinding.fullnameET.onFocusChangeListener = this
        mBinding.EmailET.onFocusChangeListener = this
        mBinding.PasswordET.onFocusChangeListener = this
        mBinding.ConfirmPasswordET.onFocusChangeListener = this
        val registerRepository = registerRepository()
        val viewModelProviderFactory = registerViewModelFactory(registerRepository,application)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(registerViewModel::class.java)

        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()


        viewModel.registerAccount.observe(this, Observer { response->

            when(response){
                is Resource.Success->{
                    alertDialog.dismiss()
                    Toast.makeText(this,response.data.toString(), Toast.LENGTH_SHORT).show()
                    val data = response.data!!.success
                    if(data){
                        navigateToSecondActivity(mBinding.EmailET.text.toString())
                    }
                    else{

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

        mBinding.RegisterBtn.setOnClickListener{
            val isvalidname = validateFullName()
            val isvalidemail = validateEmail()
            val isvalidpassword = validatePassword()
            val isvalidconfirmpassword = validateConfirmPassword()
            if(isvalidconfirmpassword&&isvalidemail&&isvalidname&&isvalidpassword) {
                val registerAccount = registerAccountModel(
                    mBinding.EmailET.text.toString(),
                    mBinding.fullnameET.text.toString(),
                    mBinding.PasswordET.text.toString()
                )
                viewModel.register(registerAccount)
            }

        }
        mBinding.login.setOnClickListener {
            finish()
        }
    }
    private fun validateFullName():Boolean{
        var errorMessage: String? = null
        val value : String = mBinding.fullnameET.text.toString()
        if(value.isEmpty()){
            errorMessage = "Full name is required"
        }
        if(errorMessage!=null){
            mBinding.fullnameTL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage==null
    }
    private fun validateEmail() : Boolean{
        var errorMessage: String? = null
        val value : String = mBinding.EmailET.text.toString()
        if(value.isEmpty()){
            errorMessage = "Email is required"
        }else if(!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            errorMessage = "Invalid email address"
        }
        if(errorMessage!=null){
            mBinding.EmailTL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }
    private fun validatePassword() : Boolean{
        var errorMessage: String? = null
        val value : String = mBinding.PasswordET.text.toString()
        if(value.isEmpty()){
            errorMessage = "Password is required"
        }else if(value.length<6){
            errorMessage = "Password must be 6 characters long "
        }
        if(errorMessage!=null){
            mBinding.PasswordTL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null
    }
    private fun validateConfirmPassword():Boolean{
        var errorMessage: String? = null
        val value : String = mBinding.ConfirmPasswordET.text.toString()
        if(value.isEmpty()){
            errorMessage = "Confirm password is required"
        }else if(value.length<6){
            errorMessage = "Confirm password must be 6 characters long "
        }
        if(errorMessage!=null){
            mBinding.ConfirmPasswordTL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage == null

    }
    private fun validatePasswordAndConfirmPassword():Boolean{
        var errorMessage: String? = null
        val ConfirmPassword : String = mBinding.ConfirmPasswordET.text.toString()
        val Password : String = mBinding.PasswordET.text.toString()
        if(Password!=ConfirmPassword){
            errorMessage = "Confirm password doesn't match with the password"
        }
        if(errorMessage!=null){
            mBinding.ConfirmPasswordTL.apply {
                isErrorEnabled = true
                error = errorMessage
            }
        }
        return errorMessage==null
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view!=null){
            when(view.id){
                R.id.fullnameET ->{
                    if(hasFocus){
                        if(mBinding.fullnameTL.isErrorEnabled){
                            mBinding.fullnameTL.isErrorEnabled = false
                        }
                    }
                    else{
                        validateFullName()
                    }
                }
                R.id.PasswordET ->{
                    if(hasFocus){
                        if(mBinding.PasswordTL.isErrorEnabled){
                            mBinding.PasswordTL.isErrorEnabled = false
                        }
                    }
                    else{
                        if(validatePassword()&&validateConfirmPassword()&&validatePasswordAndConfirmPassword()){
                            if(mBinding.ConfirmPasswordTL.isErrorEnabled){
                                mBinding.ConfirmPasswordTL.isErrorEnabled = false
                            }
                        }
                    }
                }
                R.id.EmailET ->{
                    if(hasFocus){
                        if(mBinding.EmailTL.isErrorEnabled){
                            mBinding.EmailTL.isErrorEnabled = false
                        }
                    }
                    else{
                        validateEmail()
                    }
                }
                R.id.ConfirmPasswordET ->{
                    if(hasFocus){
                        if(mBinding.ConfirmPasswordTL.isErrorEnabled){
                            mBinding.ConfirmPasswordTL.isErrorEnabled = false
                        }
                    }
                    else{
                        if(validatePassword()&&validateConfirmPassword()&&validatePasswordAndConfirmPassword()){
                            if(mBinding.PasswordTL.isErrorEnabled){
                                mBinding.PasswordTL.isErrorEnabled = false
                            }
                        }
                    }
                }

            }
        }
    }

    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }

    private fun navigateToSecondActivity(email : String) {
        val intent = Intent(this, OtpActivity::class.java)
        intent.putExtra("user_email",email)
        startActivity(intent)

    }
    private fun navigateToLoginActivity(){
        val intent = Intent(this,LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}