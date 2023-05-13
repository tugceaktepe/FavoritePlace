package com.aktepetugce.favoriteplace.login.ui.register

sealed class RegisterUiState {
    object Loading : RegisterUiState()
    object UserRegistered : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}
