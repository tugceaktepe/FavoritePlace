package com.aktepetugce.favoriteplace.location.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.domain.model.Place
import com.aktepetugce.favoriteplace.domain.model.Result
import com.aktepetugce.favoriteplace.domain.usecase.location.SavePlaceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val savePlaceUseCase: SavePlaceUseCase,

) : ViewModel() {

    private val _uiState: MutableStateFlow<MapsUiState> = MutableStateFlow(MapsUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    fun savePlace(place: Place, data: ByteArray) = viewModelScope.launch {
        savePlaceUseCase(data, place).collect { response ->
            when (response) {
                is Result.Success<Unit> -> {
                    _uiState.value = MapsUiState.LocationIsAdded
                }

                is Result.Error -> {
                    _uiState.value = MapsUiState.Error(response.message)
                }

                is Result.Loading -> {
                    _uiState.value = MapsUiState.Loading
                }
            }
        }
    }
}
