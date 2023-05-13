package com.aktepetugce.favoriteplace.home.domain.usecases

import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import com.aktepetugce.favoriteplace.common.extension.toResult
import com.aktepetugce.favoriteplace.common.model.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignOut @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<Result<Unit>> {
        return flow {
            emit(repository.signOut())
        }.toResult()
    }
}
