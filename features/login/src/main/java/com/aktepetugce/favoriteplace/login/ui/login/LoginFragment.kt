package com.aktepetugce.favoriteplace.login.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.aktepetugce.favoriteplace.core.extension.gone
import com.aktepetugce.favoriteplace.core.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.core.extension.onClick
import com.aktepetugce.favoriteplace.core.extension.showSnackbar
import com.aktepetugce.favoriteplace.core.extension.visible
import com.aktepetugce.favoriteplace.login.R
import com.aktepetugce.favoriteplace.login.databinding.FragmentLoginBinding
import com.aktepetugce.favoriteplace.uicomponents.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkUser()
        initListeners()
        subscribeObservers()
    }

    private fun initListeners() {
        with(binding) {
            buttonSignIn.onClick {
                // add validations
                val email = editTextEmail.text.toString()
                val password = editTextPassword.text.toString()
                if (viewModel.isUserNamePasswordValid(email, password)) {
                    viewModel.signIn(email, password)
                } else {
                    requireView().showSnackbar(getString(R.string.email_or_password_empty_error))
                }
                hideKeyboard()
            }
            textViewSignUp.onClick {
                findNavController().navigate(R.id.fragmentRegister)
            }
        }
    }

    private fun subscribeObservers() {
        with(binding) {
            viewModel.uiState.launchAndCollectIn(viewLifecycleOwner) { uiState ->
                when (uiState) {
                    is LoginUiState.InitalState -> {}
                    is LoginUiState.Loading -> progressBar.visible()
                    is LoginUiState.Error -> {
                        progressBar.gone()
                        requireView().showSnackbar(uiState.message)
                    }
                    is LoginUiState.UserSignedIn -> {
                        progressBar.gone()
                        navigateToHome()
                    }
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
            navOptions {
                popUpTo(R.id.fragmentLogin) { inclusive = true }
            }
        )
    }
}
