package com.aktepetugce.favoriteplace.domain.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class Result<out T> {
    object Loading : Result<Nothing>()

    data class Success<out T>(
        val data: T
    ) : Result<T>()

    data class Error(
        val message: String
    ) : Result<Nothing>()
}

fun <T> Flow<T>.toResult(isLoading: Boolean = true): Flow<Result<T>> {
    return map<T, com.aktepetugce.favoriteplace.domain.model.Result<T>> {
        com.aktepetugce.favoriteplace.domain.model.Result.Success(it)
    }
        .onStart {
            if (isLoading) {
                emit(com.aktepetugce.favoriteplace.domain.model.Result.Loading)
            }
        }
        .catch { exception ->
            emit(com.aktepetugce.favoriteplace.domain.model.Result.Error(exception.message ?: exception.toString()))
        }
}

fun <T, R> Flow<Result<T>>.mapResult(mapper: suspend (T) -> R): Flow<Result<R>> {
    return map {
        when (it) {
            is com.aktepetugce.favoriteplace.domain.model.Result.Success -> {
                val data = it.data
                com.aktepetugce.favoriteplace.domain.model.Result.Success(mapper(data))
            }

            is com.aktepetugce.favoriteplace.domain.model.Result.Loading -> it
            is com.aktepetugce.favoriteplace.domain.model.Result.Error -> it
        }
    }.catch {
        emit(com.aktepetugce.favoriteplace.domain.model.Result.Error(it.message ?: it.toString()))
    }
}
