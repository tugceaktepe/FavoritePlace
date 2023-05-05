package com.aktepetugce.favoriteplace.home.domain.usecases

import javax.inject.Inject

class SignOut @Inject constructor(
    private val repository: com.aktepetugce.favoriteplace.common.data.repo.AuthRepository
) {
    operator fun invoke() = repository.signOut()
}
