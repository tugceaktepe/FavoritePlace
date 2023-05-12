package com.aktepetugce.favoriteplace.login.ui.register

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.aktepetugce.favoriteplace.common.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.common.extension.onClick
import com.aktepetugce.favoriteplace.login.R
import com.aktepetugce.favoriteplace.login.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment :
    com.aktepetugce.favoriteplace.common.base.BaseFragment<FragmentRegisterBinding>(
        FragmentRegisterBinding::inflate
    ) {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonSignUp.onClick {
                if (editTextEmail.text.isNullOrEmpty() || editTextPassword.text.isNullOrEmpty()) {
                    showErrorMessage(getString(R.string.email_or_password_empty_error))
                } else {
                    viewModel.signUp(
                        binding.editTextEmail.text.toString(),
                        binding.editTextPassword.text.toString()
                    )
                    hideKeyboard()
                }
            }
            textViewLogin.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentRegister_to_fragmentLogin)
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) {
            viewModel.uiState.collect { uiState ->
                binding.progressBar.isVisible = uiState.isLoading
                uiState.errorMessage?.let {
                    showErrorMessage(it)
                    viewModel.userMessageShown()
                } ?: run {
                    if (uiState.success) {
                        navigateToHome()
                    }
                }
            }
        }
    }
    private fun navigateToHome() {
        val deepLinkUri = NavDeepLinkRequest.Builder
            .fromUri("android-app:/com.aktepetugce.favoriteplace/home_fragment".toUri())
            .build()
        findNavController().navigate(
            deepLinkUri,
            navOptions { // Use the Kotlin DSL for building NavOptions
                anim {
                    enter = android.R.animator.fade_in
                    exit = android.R.animator.fade_out
                }
                popUpTo(R.id.fragmentRegister) { inclusive = true }
            }
        )
    }
}
