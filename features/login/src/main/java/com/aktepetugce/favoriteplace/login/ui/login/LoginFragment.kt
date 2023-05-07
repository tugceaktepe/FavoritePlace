package com.aktepetugce.favoriteplace.login.ui.login

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
import com.aktepetugce.favoriteplace.login.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : com.aktepetugce.favoriteplace.common.base.BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate,
    false
) {
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        subscribeObservers()
    }

    private fun initListeners() {
        with(binding) {
            buttonSignIn.onClick {
                // add validations
                if (editTextEmail.text.isNullOrEmpty() || editTextPassword.text.isNullOrEmpty()) {
                    showErrorMessage(getString(R.string.email_or_password_empty_error))
                } else {
                    viewModel.signIn(
                        binding.editTextEmail.text.toString(),
                        binding.editTextPassword.text.toString()
                    )
                    hideKeyboard()
                }
                hideKeyboard()
            }
            textViewSignUp.onClick {
                findNavController().navigate(R.id.fragmentRegister)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { uiState ->
            binding.progressBar.isVisible = uiState.isLoading
            uiState.errorMessage?.let {
                showErrorMessage(it)
                viewModel.userMessageShown()
            }
            if (uiState.isUserAuthenticated) {
                navigateToHome()
            }
            if (uiState.isLoginSuccess) {
                navigateToHome()
            }
        }
    }

    private fun navigateToHome() {
        val deepLinkUri = NavDeepLinkRequest.Builder
            .fromUri("android-app:/com.aktepetugce.favoriteplace/home".toUri())
            .build()
        findNavController().navigate(
            deepLinkUri,
            navOptions {
                popUpTo(R.id.fragmentLogin) { inclusive = true }
            }
        )
    }
}
