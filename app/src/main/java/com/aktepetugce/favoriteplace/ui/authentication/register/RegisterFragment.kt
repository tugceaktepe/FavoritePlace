package com.aktepetugce.favoriteplace.ui.authentication.register

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.base.BaseFragment
import com.aktepetugce.favoriteplace.databinding.FragmentRegisterBinding
import com.aktepetugce.favoriteplace.util.extension.launchAndCollectIn
import com.aktepetugce.favoriteplace.util.extension.onClick
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment :
    BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate, false) {
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
                }
                uiState.nextDestination?.let { nextPage ->
                    findNavController().navigate(nextPage)
                }
            }
        }
    }
}
