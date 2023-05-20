package com.aktepetugce.favoriteplace.login.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.aktepetugce.favoriteplace.domain.model.Result

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<RegisterUiState?> = MutableStateFlow(null)
    val uiState = _uiState.asStateFlow()

    fun signUp(userEmail: String, password: String) = viewModelScope.launch {
        signUpUseCase.invoke(userEmail, password).collect { response ->
            when (response) {
                is Result.Success<*> -> {
                    _uiState.update { RegisterUiState.UserRegistered }
                }

                is Result.Error -> {
                    _uiState.update { RegisterUiState.Error(message = response.message) }
                }

                else -> {
                    _uiState.update { RegisterUiState.Loading }
                }
            }
        }
    }

    // TODO: move logic to usecase layer and change validation
    fun isUserNamePasswordValid(email: String, password: String): Boolean {
        return !(email.isEmpty() || password.isEmpty())
    }
}
