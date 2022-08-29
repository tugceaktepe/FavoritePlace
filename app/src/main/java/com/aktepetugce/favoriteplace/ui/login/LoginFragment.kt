package com.aktepetugce.favoriteplace.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate, false) {
    private lateinit var viewModel: LoginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        with(binding) {
            buttonSignIn.setOnClickListener {
                if (emailEditText.text.isNullOrEmpty() || editTextPassword.text.isNullOrEmpty()) {
                    showErrorMessage(getString(R.string.email_or_password_empty_error))
                } else {
                    viewModel.signIn(
                        binding.emailEditText.text.toString(),
                        binding.editTextPassword.text.toString()
                    )
                    hideKeyboard()
                }
            }
            signUpTextView2.setOnClickListener {
                findNavController().navigate(R.id.action_login_to_register)
            }
        }
        setObservers()
    }
    private fun setObservers(){
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        viewModel.isSignInSuccess.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_login_to_home)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            showErrorMessage(it)
        }
    }

}