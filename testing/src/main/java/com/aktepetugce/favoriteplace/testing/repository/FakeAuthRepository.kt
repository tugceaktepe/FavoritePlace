package com.aktepetugce.favoriteplace.testing.repository

import com.aktepetugce.favoriteplace.data.repository.AuthRepository
import javax.inject.Inject

class FakeAuthRepository @Inject constructor(): AuthRepository {
    override fun isUserAuthenticatedInFirebase(): Boolean {
        return true
    }

    override suspend fun signUp(userEmail: String, password: String) {
        //
    }

    override suspend fun signIn(userEmail: String, password: String) {
        if (!userEmail.contains("@")) {
            throw NullPointerException("Sign Error")
        }
    }

    override suspend fun signOut() { //
    }

    override fun getCurrentUserEmail(): String {
        return TEST_EMAIL
    }
    companion object {
        const val TEST_EMAIL = "test@mail.com"
    }
}
