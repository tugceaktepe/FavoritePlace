package com.aktepetugce.favoriteplace.home.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.domain.model.Result
import com.aktepetugce.favoriteplace.domain.usecase.authentication.SignOutUseCase
import com.aktepetugce.favoriteplace.domain.usecase.location.FetchPlacesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchPlaceUseCase: FetchPlacesUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    fun fetchPlaces(isLoading: Boolean = true) = viewModelScope.launch {
        fetchPlaceUseCase(isLoading).collect { response ->
            when (response) {
                is Result.Success<*> -> {
                    _uiState.update {
                        HomeUiState.PlaceListLoaded(
                            placeList = response.data as List<Place>
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        HomeUiState.Error(
                            message = response.message,
                            isNotShown = true
                        )
                    }
                }

                else -> {
                    _uiState.update { HomeUiState.Loading }
                }
            }
        }
    }

    fun signOut() = viewModelScope.launch {
        signOutUseCase().collect { response ->
            when (response) {
                is Result.Success<*> -> {
                    _uiState.update { HomeUiState.UserSignedOut }
                }

                is Result.Error -> {
                    _uiState.update {
                        HomeUiState.Error(
                            message = response.message,
                            isNotShown = true
                        )
                    }
                }

                else -> {
                    _uiState.update { HomeUiState.Loading }
                }
            }
        }
    }

    fun errorMessageShown() {
        _uiState.update { HomeUiState.Error(message = "", isNotShown = false) }
    }
}
