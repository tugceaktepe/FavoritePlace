package com.aktepetugce.favoriteplace.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.common.model.Response
import com.aktepetugce.favoriteplace.domain.model.UIPlace
import com.aktepetugce.favoriteplace.domain.usecase.authentication.AuthUseCases
import com.aktepetugce.favoriteplace.domain.usecase.place.PlaceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val placeUseCases: PlaceUseCases,
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeViewState())
    val uiState: StateFlow<HomeViewState> = _uiState

    fun signOut() = viewModelScope.launch {
        authUseCases.signOut().collect { response ->
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
        val email = authUseCases.currentUserEmail.invoke()
        placeUseCases.fetchPlaces(email).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    _uiState.update { currentState ->
                        currentState.copy(
                            placeList = response.data as List<UIPlace>?,
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
