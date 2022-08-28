package com.aktepetugce.favoriteplace.domain.usecase.authentication

import com.aktepetugce.favoriteplace.data.repo.AuthRepository

class SignIn (
    private val repository: AuthRepository
) {
    operator fun invoke(userEmail : String, password : String) = repository.signIn(userEmail, password)
}