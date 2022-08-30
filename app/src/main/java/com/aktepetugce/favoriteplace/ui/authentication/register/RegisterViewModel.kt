package com.aktepetugce.favoriteplace.ui.authentication.register

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
class RegisterViewModel @Inject constructor(private val authUseCases: AuthUseCases) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterViewState(registerStarted = false))
    val uiState: StateFlow<RegisterViewState> = _uiState

    fun signUp(userEmail: String, password: String) = viewModelScope.launch {
        authUseCases.signUp(userEmail, password).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            nextDestination = R.id.action_register_to_home,
                            registerStarted = false
                        )
                    }
                }
                is Response.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = response.message, registerStarted = false)
                    }
                }
                else -> {
                    _uiState.update { currentState ->
                        currentState.copy(registerStarted = true)
                    }
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.value = RegisterViewState(
            errorMessage = null
        )
    }
}