package com.aktepetugce.favoriteplace.ui.authentication.register

data class RegisterViewState(
    val success: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
