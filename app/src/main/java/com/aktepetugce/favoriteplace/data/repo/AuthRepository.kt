package com.aktepetugce.favoriteplace.data.repo

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun isUserAuthenticatedInFirebase(): Boolean
    fun signUp(userEmail: String, password: String): Flow<Any>
    fun signIn(userEmail: String, password: String): Flow<Any>
    fun signOut(): Flow<Any>
    fun getCurrentUserEmail(): String
}
