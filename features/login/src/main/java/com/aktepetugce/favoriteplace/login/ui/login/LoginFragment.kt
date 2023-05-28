package com.aktepetugce.favoriteplace.login.ui.login

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.aktepetugce.favoriteplace.core.extension.hide
import com.aktepetugce.favoriteplace.core.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.core.extension.onClick
import com.aktepetugce.favoriteplace.core.extension.show
import com.aktepetugce.favoriteplace.core.extension.showSnackbar
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
                    is LoginUiState.UserNotSignedIn -> {
                        progressBar.hide()
                    }
                    is LoginUiState.Loading -> {
                        progressBar.show()
                    }
                    is LoginUiState.Error -> {
                        progressBar.hide()
                        requireView().showSnackbar(uiState.message)
                    }
                    is LoginUiState.UserSignedIn -> {
                        progressBar.hide()
                        navigateToHome()
                    }
                    else -> {}
                }
            }
        }
    }

    private fun navigateToHome() {
        findNavController().navigate(
            Uri.parse("android-app:/com.aktepetugce.favoriteplace/home"),
            navOptions {
                popUpTo(R.id.fragmentLogin) { inclusive = true }
            }
        )
    }
}
