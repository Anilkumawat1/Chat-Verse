package com.anilkumawat.chatverse.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.anilkumawat.chatverse.R
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anilkumawat.chatverse.databinding.ActivityLoginBinding
import com.anilkumawat.chatverse.model.loginModel
import com.anilkumawat.chatverse.model.loginUserModel
import com.anilkumawat.chatverse.repository.loginRepository
import com.anilkumawat.chatverse.utils.SessionManager
import com.anilkumawat.chatverse.viewmodel.loginViewModel
import com.anilkumawat.chatverse.viewmodelfactory.loginViewModelFactory
import com.anilkumawat.mvvmloginsignup.utils.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson

class LoginActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {
   private lateinit var mBinding: ActivityLoginBinding
   private lateinit var viewModel: loginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        mBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        val loginRepository = loginRepository()
        val viewModelProviderFactory = loginViewModelFactory(loginRepository,application)
        viewModel = ViewModelProvider(this,viewModelProviderFactory).get(loginViewModel::class.java)
        mBinding.EmailET.onFocusChangeListener = this
        mBinding.PasswordET.onFocusChangeListener = this

        mBinding.loginBtn.setOnClickListener {
            val isvalidEmail = validateEmail()
            val isvalidPassword = validatePassword()
            if(isvalidEmail&&isvalidPassword){
                val loginModel = loginModel(mBinding.EmailET.text.toString(),mBinding.PasswordET.text.toString())
                viewModel.login(loginModel)
            }
        }
        mBinding.signup.setOnClickListener {
            navigateToSignupActivity()
        }
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null)

        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()
        viewModel.loginResponse.observe(this, Observer { response->
            when(response){
                is Resource.Success ->{
                    alertDialog.dismiss()
                    val data = response.data!!.success
                    if(data){

                        val gson = Gson()

                        // Parse JSON string to User object
                        val user: loginUserModel = gson.fromJson(response.data.data.toString(), loginUserModel::class.java)
                        showSnackbar("Successfully login")
                        SessionManager.saveAuthToken(this,user.user.authToken)
                        SessionManager.saveString(this,"user_name",user.user.name)
                        SessionManager.saveString(this,"user_id",user.user._id)
                        SessionManager.saveString(this,"user_email",user.user.email)
                        navigateToMainActivity()
                    }
                    else{
                        showSnackbar(response.data.message[0].toString())
                    }
                }
                is Resource.Error->{
                    alertDialog.dismiss()
                        showSnackbar(response.message.toString())
                }
                is Resource.Loading->{
                    alertDialog.show()
                }
            }
        })
        mBinding.forgotPassword.setOnClickListener {
            navigateToForgotPasswordActivity()
        }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToSignupActivity(){
        val intent = Intent(this,SignUpActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToForgotPasswordActivity(){
        val intent = Intent(this,ForgotPassword::class.java)
        startActivity(intent)
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

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view!=null){
            when(view.id){
                R.id.EmailET->{
                    if(hasFocus) {
                        if (mBinding.EmailTL.isErrorEnabled) {
                            mBinding.EmailTL.isErrorEnabled = false
                        }
                    }
                    else{
                        validateEmail()
                    }
                }
                R.id.PasswordET->{
                    if(hasFocus) {
                        if (mBinding.PasswordTL.isErrorEnabled) {
                            mBinding.PasswordTL.isErrorEnabled = false
                        }
                    }
                    else{
                        validatePassword()
                    }
                }
            }
        }
    }
    private fun showSnackbar(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }
    override fun onKey(p0: View?, p1: Int, p2: KeyEvent?): Boolean {
        TODO("Not yet implemented")
    }
}
