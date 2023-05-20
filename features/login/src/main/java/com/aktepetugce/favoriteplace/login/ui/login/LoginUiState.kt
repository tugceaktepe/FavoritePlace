package com.aktepetugce.favoriteplace.login.ui.login

sealed class LoginUiState {
    object InitalState : LoginUiState()
    object Loading : LoginUiState()
    object UserSignedIn : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
