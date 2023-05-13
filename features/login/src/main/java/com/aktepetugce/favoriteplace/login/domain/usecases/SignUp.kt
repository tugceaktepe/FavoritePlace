package com.aktepetugce.favoriteplace.login.domain.usecases

import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.common.extension.toResult
import com.aktepetugce.favoriteplace.common.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignUp @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(userEmail: String, password: String): Flow<Result<Unit>> {
        return flow {
            emit(repository.signUp(userEmail, password))
        }.toResult()
    }
}
