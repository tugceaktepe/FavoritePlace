package com.aktepetugce.favoriteplace.domain.usecase.authentication

import com.aktepetugce.favoriteplace.data.repository.AuthRepository
import com.aktepetugce.favoriteplace.domain.model.Result
import com.aktepetugce.favoriteplace.domain.model.toResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckUserAuthenticatedUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Result<Boolean>> = flow {
        emit(repository.isUserAuthenticatedInFirebase())
    }.toResult()
}
