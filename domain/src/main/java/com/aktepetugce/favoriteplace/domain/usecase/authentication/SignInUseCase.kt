package com.aktepetugce.favoriteplace.domain.usecase.authentication

import com.aktepetugce.favoriteplace.data.repository.AuthRepository
import com.aktepetugce.favoriteplace.domain.model.Result
import com.aktepetugce.favoriteplace.domain.model.toResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(userEmail: String, password: String): Flow<Result<Unit>> {
        return flow {
            emit(repository.signIn(userEmail, password))
        }.toResult()
    }
}
