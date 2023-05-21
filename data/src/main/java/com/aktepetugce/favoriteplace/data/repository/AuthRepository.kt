package com.aktepetugce.favoriteplace.data.repository

interface AuthRepository {
    suspend fun isUserAuthenticatedInFirebase(): Boolean
    suspend fun signUp(userEmail: String, password: String)
    suspend fun signIn(userEmail: String, password: String)
    suspend fun signOut()
    fun getCurrentUserEmail(): String
}
