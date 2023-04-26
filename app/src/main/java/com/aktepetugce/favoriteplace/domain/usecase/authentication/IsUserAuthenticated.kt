package com.aktepetugce.favoriteplace.domain.usecase.authentication

import com.aktepetugce.favoriteplace.data.repo.AuthRepository

class IsUserAuthenticated(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.isUserAuthenticatedInFirebase()
}
