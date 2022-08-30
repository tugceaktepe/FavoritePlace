package com.aktepetugce.favoriteplace.ui.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.domain.usecase.authentication.AuthUseCases
import com.aktepetugce.favoriteplace.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginViewState())
    val uiState: StateFlow<LoginViewState> = _uiState

    fun signIn(userEmail: String, password: String) = viewModelScope.launch {
        authUseCases.signIn(userEmail, password).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            nextDestination = R.id.action_login_to_home,
                            isLoading = false
                        )
                    }
                }
                is Response.Error -> {
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