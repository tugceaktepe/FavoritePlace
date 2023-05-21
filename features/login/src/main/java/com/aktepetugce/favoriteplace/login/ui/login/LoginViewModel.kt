package com.aktepetugce.favoriteplace.login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.domain.model.Result
import com.aktepetugce.favoriteplace.domain.usecase.authentication.CheckUserAuthenticatedUseCase
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val checkUserAuthenticatedUseCase: CheckUserAuthenticatedUseCase,

) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState.InitalState)
    val uiState = _uiState.asStateFlow()

    fun checkUser() = viewModelScope.launch {
        checkUserAuthenticatedUseCase().collect { isUserAuthenticated ->
            if (isUserAuthenticated) {
                _uiState.update { LoginUiState.UserSignedIn }
            }
        }
    }

    fun signIn(userEmail: String, password: String) = viewModelScope.launch {
        signInUseCase.invoke(userEmail, password).collect { response ->
            when (response) {
                is Result.Success<*> -> {
                    _uiState.update { LoginUiState.UserSignedIn }
                }

                is Result.Error -> {
                    _uiState.update { LoginUiState.Error(message = response.message) }
                }

                else -> {
                    _uiState.update { LoginUiState.Loading }
                }
            }
        }
    }

    // TODO: move logic to usecase layer and change validation
    fun isUserNamePasswordValid(email: String, password: String): Boolean {
        return !(email.isEmpty() || password.isEmpty())
    }
}
