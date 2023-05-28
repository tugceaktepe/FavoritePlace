package com.aktepetugce.favoriteplace.login.ui.register

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
import com.aktepetugce.favoriteplace.login.databinding.FragmentRegisterBinding
import com.aktepetugce.favoriteplace.uicomponents.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(
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
                    is RegisterUiState.Loading -> progressBar.show()
                    is RegisterUiState.Error -> {
                        progressBar.hide()
                        requireView().showSnackbar(uiState.message)
                    }

                    is RegisterUiState.UserRegistered -> {
                        progressBar.hide()
                        navigateToHome()
                    }

                    else -> {}
                }
            }
        }
    }
    private fun navigateToHome() {
        val deepLinkUri = Uri.parse("android-app:/com.aktepetugce.favoriteplace/home")
        findNavController().navigate(
            deepLinkUri,
            navOptions { // Use the Kotlin DSL for building NavOptions
                popUpTo(R.id.fragmentRegister) { inclusive = true }
            }
        )
    }
}
