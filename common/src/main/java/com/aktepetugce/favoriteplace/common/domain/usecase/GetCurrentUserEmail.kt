package com.aktepetugce.favoriteplace.common.domain.usecase

import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import javax.inject.Inject

class GetCurrentUserEmail @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.getCurrentUserEmail()
}
