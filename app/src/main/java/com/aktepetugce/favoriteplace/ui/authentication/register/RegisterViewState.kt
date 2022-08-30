package com.aktepetugce.favoriteplace.ui.authentication.register

data class RegisterViewState(
    val nextDestination : Int? = null ,
    val registerStarted: Boolean = false,
    val errorMessage: String? = null
)
