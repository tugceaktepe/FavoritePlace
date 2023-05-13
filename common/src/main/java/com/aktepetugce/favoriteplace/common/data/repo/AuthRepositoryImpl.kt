package com.aktepetugce.favoriteplace.common.data.repo

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher,
) : AuthRepository {
    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun signUp(userEmail: String, password: String): Unit =
        withContext(dispatcher) {
            auth.createUserWithEmailAndPassword(userEmail, password).await()
        }

    override suspend fun signIn(userEmail: String, password: String): Unit =
        withContext(dispatcher) {
            auth.signInWithEmailAndPassword(userEmail, password).await()
        }

    override suspend fun signOut() = withContext(dispatcher) {
        auth.signOut()
    }

    override fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }
}
