package com.aktepetugce.favoriteplace.testing.repository

import com.aktepetugce.favoriteplace.data.repository.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun isUserAuthenticatedInFirebase(): Boolean {
        delay(API_CALL_TIME)
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
        const val API_CALL_TIME = 1000L
    }
}
