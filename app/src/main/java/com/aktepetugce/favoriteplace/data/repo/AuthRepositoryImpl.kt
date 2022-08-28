package com.aktepetugce.favoriteplace.data.repo

import com.aktepetugce.favoriteplace.util.Response
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
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

    override fun signUp(userEmail: String, password: String) = flow<Response<*>> {
        emit(Response.Loading)
        auth.createUserWithEmailAndPassword(userEmail, password).await()
        emit(Response.Success(true))
    }.catch {
        emit(Response.Error(it.message ?: it.toString()))
    }.flowOn(dispatcher)

    override fun signIn(userEmail: String, password: String) = flow<Response<*>> {
        emit(Response.Loading)
        auth.signInWithEmailAndPassword(userEmail, password).await()
        emit(Response.Success(true))
    }.catch {
        emit(Response.Error(it.message ?: it.toString()))
    }.flowOn(dispatcher)


    override fun signOut() = flow {
        emit(Response.Loading)
        auth.signOut()
        emit(Response.Success(true))
    }.catch {
        emit(Response.Error(it.message ?: it.toString()))
    }.flowOn(dispatcher)

    override fun getCurrentUserEmail(): String {
        return auth.currentUser?.email ?: ""
    }
}