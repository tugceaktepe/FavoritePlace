package com.aktepetugce.favoriteplace.data.repo

import com.aktepetugce.favoriteplace.util.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
private val auth: FirebaseAuth
): AuthRepository {
    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }
    override fun signUp(userEmail: String, password: String) = flow {
        try {
            emit(Response.Loading)
            auth.createUserWithEmailAndPassword(userEmail,password).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }
    override fun signIn(userEmail: String, password: String) = flow {
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(userEmail,password).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override fun signOut() = flow {
        try {
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }

}