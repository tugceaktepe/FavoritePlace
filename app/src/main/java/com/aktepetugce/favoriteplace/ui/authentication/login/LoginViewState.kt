package com.aktepetugce.favoriteplace.ui.authentication.login

data class LoginViewState(
    val isUserAuthenticated: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
