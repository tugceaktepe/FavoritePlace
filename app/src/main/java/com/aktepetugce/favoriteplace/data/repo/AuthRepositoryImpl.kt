package com.aktepetugce.favoriteplace.data.repo

import com.aktepetugce.favoriteplace.util.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val dispatcher: CoroutineDispatcher,
) : AuthRepository {
    override fun isUserAuthenticatedInFirebase(): Boolean {
        return auth.currentUser != null
    }

    override fun signUp(userEmail: String, password: String) = flow<Response<*>> {
        auth.createUserWithEmailAndPassword(userEmail, password).await()
        emit(Response.Success(true))
    }.flowOn(dispatcher)
        .onStart { emit(Response.Loading) }
        .catch { emit(Response.Error(it.message ?: it.toString())) }

    override fun signIn(userEmail: String, password: String) = flow<Response<*>> {
        auth.signInWithEmailAndPassword(userEmail, password).await()
        emit(Response.Success(true))
    }.flowOn(dispatcher)
        .onStart { emit(Response.Loading) }
        .catch { emit(Response.Error(it.message ?: it.toString())) }

    override fun signOut() = flow<Response<*>> {
        auth.signOut()
        emit(Response.Success(true))
    }.flowOn(dispatcher)
        .onStart { emit(Response.Loading) }
        .catch { emit(Response.Error(it.message ?: it.toString())) }

    override fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }
}