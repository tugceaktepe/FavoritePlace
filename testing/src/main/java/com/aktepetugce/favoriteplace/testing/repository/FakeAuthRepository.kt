package com.aktepetugce.favoriteplace.testing.repository

import com.aktepetugce.favoriteplace.data.repository.AuthRepository
import com.aktepetugce.favoriteplace.testing.util.constant.CommonConstants.API_CALL_TIME
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.SIGN_IN_ERROR
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.SIGN_OUT_ERROR
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.SIGN_UP_ERROR
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants.TEST_EMAIL
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakeAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun isUserAuthenticatedInFirebase(): Boolean {
        return isAuthenticated
    }

    override suspend fun signUp(userEmail: String, password: String) {
        delay(API_CALL_TIME)
        if (!userEmail.contains("@")) {
            throw IllegalArgumentException(SIGN_UP_ERROR)
        }
    }

    override suspend fun signIn(userEmail: String, password: String) {
        delay(API_CALL_TIME)
        if (!userEmail.contains("@")) {
            throw IllegalArgumentException(SIGN_IN_ERROR)
        }
    }

    override suspend fun signOut() {
        delay(API_CALL_TIME)
        if (!isAuthenticated) {
            throw IllegalStateException(SIGN_OUT_ERROR)
        }
        isAuthenticated = false
    }

    override fun getCurrentUserEmail(): String {
        return if (!isAuthenticated) {
            ""
        } else {
            TEST_EMAIL
        }
    }
    companion object {
        var isAuthenticated = false
    }
}
