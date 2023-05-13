package com.aktepetugce.favoriteplace.login.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.common.model.Result
import com.aktepetugce.favoriteplace.login.domain.usecases.IsUserAuthenticated
import com.aktepetugce.favoriteplace.login.domain.usecases.SignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignIn,
    private val checkUserAuthenticated: IsUserAuthenticated,

) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState?> = MutableStateFlow(null)
    val uiState = _uiState.asStateFlow()

    fun checkUser() = viewModelScope.launch {
        checkUserAuthenticated().collect { isUserAuthenticated ->
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
