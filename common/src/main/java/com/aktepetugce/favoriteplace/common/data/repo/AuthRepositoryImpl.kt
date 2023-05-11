package com.aktepetugce.favoriteplace.common.data.repo

import com.aktepetugce.favoriteplace.common.extension.toResult
import com.aktepetugce.favoriteplace.common.model.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher,
) : AuthRepository {
    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

    override fun signUp(userEmail: String, password: String) = flow<Result<*>> {
        auth.createUserWithEmailAndPassword(userEmail, password).await()
        emit(Result.Success(true))
    }.flowOn(dispatcher)
        .toResult()

    override fun signIn(userEmail: String, password: String) = flow<Result<*>> {
        auth.signInWithEmailAndPassword(userEmail, password).await()
        emit(Result.Success(true))
    }.flowOn(dispatcher)
        .toResult()

    override fun signOut() = flow<Result<*>> {
        auth.signOut()
        emit(Result.Success(true))
    }.flowOn(dispatcher)
        .toResult()

    override fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }
}
