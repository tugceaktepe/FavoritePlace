package com.aktepetugce.favoriteplace.login.ui.register

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.aktepetugce.favoriteplace.common.extension.gone
import com.aktepetugce.favoriteplace.common.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.common.extension.onClick
import com.aktepetugce.favoriteplace.common.extension.showSnackbar
import com.aktepetugce.favoriteplace.common.extension.visible
import com.aktepetugce.favoriteplace.login.R
import com.aktepetugce.favoriteplace.login.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment :
    com.aktepetugce.favoriteplace.common.ui.BaseFragment<FragmentRegisterBinding>(
        FragmentRegisterBinding::inflate
    ) {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            buttonSignUp.onClick {
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                if (viewModel.isUserNamePasswordValid(email, password)) {
                    viewModel.signUp(email, password)
                } else {
                    requireView().showSnackbar(getString(R.string.email_or_password_empty_error))
                }
                hideKeyboard()
            }
            textViewLogin.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentRegister_to_fragmentLogin)
            }
        }
        subscribeObservers()
    }

    private fun subscribeObservers() {
        with(binding) {
            viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is RegisterUiState.Loading -> progressBar.visible()
                    is RegisterUiState.Error -> {
                        progressBar.gone()
                        requireView().showSnackbar(uiState.message)
                    }

                    is RegisterUiState.UserRegistered -> {
                        progressBar.gone()
                        navigateToHome()
                    }

                    else -> {}
                }
            }
        }
    }
    private fun navigateToHome() {
        val deepLinkUri = NavDeepLinkRequest.Builder
            .fromUri("android-app:/com.aktepetugce.favoriteplace/home".toUri())
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
