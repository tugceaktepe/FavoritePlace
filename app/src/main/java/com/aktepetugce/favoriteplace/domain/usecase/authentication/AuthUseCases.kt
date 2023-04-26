package com.aktepetugce.favoriteplace.domain.usecase.authentication

data class AuthUseCases(
    val isUserAuthenticated: IsUserAuthenticated,
    val signOut: SignOut,
    val signIn: SignIn,
    val signUp: SignUp,
    val currentUserEmail: GetCurrentUserEmail
)
