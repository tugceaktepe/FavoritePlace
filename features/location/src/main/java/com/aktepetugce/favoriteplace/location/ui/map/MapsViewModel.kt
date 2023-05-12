package com.aktepetugce.favoriteplace.location.ui.map

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.common.domain.model.Place
import com.aktepetugce.favoriteplace.common.model.Result
import com.aktepetugce.favoriteplace.location.domain.usecase.SavePlaceImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val savePlaceImageUseCase: SavePlaceImage,

    ) : ViewModel() {

    private val _uiState = MutableStateFlow(MapsViewState())
    val uiState: StateFlow<MapsViewState> = _uiState

    fun savePlace(place: Place, uri: Uri) = viewModelScope.launch {
        savePlaceImageUseCase.invoke(uri, place).collect { response ->
            when (response) {
                is Result.Success<*> -> {
                    _uiState.update { currentState ->
                        currentState.copy(success = true, isLoading = false)
                    }
                }

                is Result.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = response.message, isLoading = false)
                    }
                }

                is Result.Loading -> {
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
