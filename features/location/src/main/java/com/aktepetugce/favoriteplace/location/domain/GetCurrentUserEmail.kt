package com.aktepetugce.favoriteplace.location.domain

import com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
import javax.inject.Inject

class GetCurrentUserEmail @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.getCurrentUserEmail()
}
