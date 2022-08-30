package com.aktepetugce.favoriteplace.ui.authentication.register

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate, false) {
    private lateinit var viewModel: RegisterViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        with(binding) {
            buttonSignUp.setOnClickListener {
                if (emailEditText.text.isNullOrEmpty() || editTextPassword.text.isNullOrEmpty()) {
                    showErrorMessage(getString(R.string.email_or_password_empty_error))
                } else {
                    viewModel.signUp(
                        binding.emailEditText.text.toString(),
                        binding.editTextPassword.text.toString()
                    )
                    hideKeyboard()
                }
            }
        }
        setObservers()
    }
    private fun setObservers(){
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.isVisible = it
        }
        viewModel.isSignUpSuccess.observe(viewLifecycleOwner) {
            findNavController().navigate(R.id.action_register_to_home)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            showErrorMessage(it)
        }
    }

}