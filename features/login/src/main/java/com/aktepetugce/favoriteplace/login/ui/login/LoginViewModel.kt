package com.aktepetugce.favoriteplace.login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.login.domain.usecases.IsUserAuthenticated
import com.aktepetugce.favoriteplace.login.domain.usecases.SignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignIn,
    private val checkUserAuthenticated: IsUserAuthenticated,

) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState: StateFlow<LoginViewState> = _uiState

    init {
        _uiState.update { currentState ->
            currentState.copy(
                isUserAuthenticated = checkUserAuthenticated.invoke()
            )
        }
    }

    fun signIn(userEmail: String, password: String) = viewModelScope.launch {
        signInUseCase.invoke(userEmail, password).collect { response ->
            when (response) {
                is com.aktepetugce.favoriteplace.common.model.Resource.Result.Success<*> -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoginSuccess = true,
                            isLoading = false
                        )
                    }
                }

                is com.aktepetugce.favoriteplace.common.model.Resource.Result.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = response.message, isLoading = false)
                    }
                }

                else -> {
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}
