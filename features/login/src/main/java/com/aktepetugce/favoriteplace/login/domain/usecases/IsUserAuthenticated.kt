package com.aktepetugce.favoriteplace.login.domain.usecases

import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import javax.inject.Inject

class IsUserAuthenticated @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.isUserAuthenticatedInFirebase()
}
