package com.aktepetugce.favoriteplace.ui.authentication.login

data class LoginViewState(
    val nextDestination : Int? = null ,
    val loginStarted: Boolean = false,
    val errorMessage: String? = null
)



