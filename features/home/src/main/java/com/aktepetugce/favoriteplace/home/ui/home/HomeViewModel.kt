package com.aktepetugce.favoriteplace.home.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.common.data.model.Place
import com.aktepetugce.favoriteplace.common.domain.usecases.GetCurrentUserEmail
import com.aktepetugce.favoriteplace.common.model.Response
import com.aktepetugce.favoriteplace.home.domain.usecases.FetchPlaces
import com.aktepetugce.favoriteplace.home.domain.usecases.SignOut
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchPlaceUseCase: FetchPlaces,
    private val getCurrentUseEmailUseCase: GetCurrentUserEmail,
    private val signOutUseCase: SignOut
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState

    fun signOut() = viewModelScope.launch {
        signOutUseCase.invoke().collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    clearSession()
                }
                is Response.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = response.message, isLoading = false)
                    }
                }
                else -> {
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = true)
                    }
                }
            }
        }
    }

    private fun clearSession() {
        _uiState.update { currentState ->
            currentState.copy(
                placeList = listOf(),
                errorMessage = null,
                isLoading = false,
                placesLoaded = false,
                signOutSuccess = true
            )
        }
    }

    fun fetchPlaces() = viewModelScope.launch {
        val email = getCurrentUseEmailUseCase.invoke()
        fetchPlaceUseCase.invoke(email).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            placeList = response.data as List<Place>?,
                            placesLoaded = true,
                            isLoading = false
                        )
                    }
                }
                is Response.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = response.message, isLoading = false)
                    }
                }
                else -> {
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun userMessageShown() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = null)
        }
    }
}
