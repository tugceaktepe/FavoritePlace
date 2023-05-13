package com.aktepetugce.favoriteplace.home.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.common.domain.model.Place
import com.aktepetugce.favoriteplace.common.model.Result
import com.aktepetugce.favoriteplace.home.domain.usecases.FetchPlaces
import com.aktepetugce.favoriteplace.home.domain.usecases.SignOut
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchPlaceUseCase: FetchPlaces,
    private val signOutUseCase: SignOut
) : ViewModel() {

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    fun fetchPlaces() = viewModelScope.launch {
        fetchPlaceUseCase().collect { response ->
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
