package com.aktepetugce.favoriteplace.domain.usecase.authentication

import com.aktepetugce.favoriteplace.data.repo.AuthRepository

class GetCurrentUserEmail(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.getCurrentUserEmail()
}
