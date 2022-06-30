package com.aktepetugce.favoriteplace.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aktepetugce.favoriteplace.databinding.ActivityLoginBinding
import com.aktepetugce.favoriteplace.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)
        if(viewModel.isUserAuthenticated){
            navigateHome()
        }
        with(binding) {
            buttonSignIn.setOnClickListener {
                if (emailEditText.text.isNullOrEmpty() || passwordEditText.text.isNullOrEmpty()) {
                    Toast.makeText(this@LoginActivity,"Email and password can not be empty!", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.signIn(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                }
            }
            buttonSignUp.setOnClickListener {
                if (emailEditText.text.isNullOrEmpty() || passwordEditText.text.isNullOrEmpty()) {
                    Toast.makeText(this@LoginActivity,"Email and password can not be empty!", Toast.LENGTH_LONG).show()
                } else {
                    viewModel.signUp(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    )
                }
            }
        }
        setObservers()
    }
    private fun setObservers(){
        viewModel.isLoading.observe(this , Observer {
            if(it){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
            }
        })
        viewModel.isSignInSuccess.observe(this , Observer {
            navigateHome()
        })
        viewModel.isSignUpSuccess.observe(this , Observer {
            navigateHome()
        })
        viewModel.error.observe(this , Observer {
            Toast.makeText(this,it.toString(), Toast.LENGTH_LONG).show()
        })
    }

    private fun navigateHome(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }

}