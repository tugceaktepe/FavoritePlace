package com.aktepetugce.favoriteplace.login.domain.usecases

import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class IsUserAuthenticated @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Boolean> = flow {
        emit(repository.isUserAuthenticatedInFirebase())
    }
}
