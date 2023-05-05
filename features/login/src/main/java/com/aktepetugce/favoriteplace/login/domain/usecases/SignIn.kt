package com.aktepetugce.favoriteplace.login.domain.usecases

import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import javax.inject.Inject

class SignIn @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(userEmail: String, password: String) = repository.signIn(userEmail, password)
}
