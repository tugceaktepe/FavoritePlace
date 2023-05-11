package com.aktepetugce.favoriteplace.common.extension

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aktepetugce.favoriteplace.common.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

inline fun <T> Flow<T>.launchAndCollectIn(
    owner: LifecycleOwner,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline action: suspend CoroutineScope.(T) -> Unit
) = owner.lifecycleScope.launch {
    owner.repeatOnLifecycle(minActiveState) {
        collect {
            action(it)
        }
    }
}

fun <T> Flow<T>.toResult(isLoading: Boolean = true): Flow<Result<T>> {
    return map<T, Result<T>> {
        Result.Success(it)
    }
        .onStart {
            if (isLoading) {
                emit(Result.Loading)
            }
        }
        .catch { exception ->
            emit(Result.Error(exception.message ?: exception.toString()))
        }
}
