package com.aktepetugce.favoriteplace.domain.usecase.authentication

import com.aktepetugce.favoriteplace.data.repo.AuthRepository

class SignUp(
    private val repository: AuthRepository
) {
    operator fun invoke(userEmail: String, password: String) = repository.signUp(userEmail, password)
}
