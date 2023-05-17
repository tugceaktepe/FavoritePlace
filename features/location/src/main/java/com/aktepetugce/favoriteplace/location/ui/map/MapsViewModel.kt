package com.aktepetugce.favoriteplace.location.ui.map

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.common.domain.model.Place
import com.aktepetugce.favoriteplace.common.model.Result
import com.aktepetugce.favoriteplace.location.domain.usecase.SavePlace
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val savePlaceUseCase: SavePlace,

    ) : ViewModel() {

    private val _uiState: MutableStateFlow<MapsUiState> = MutableStateFlow(MapsUiState.InitialState)
    val uiState = _uiState.asStateFlow()

    fun savePlace(place: Place, uri: Uri) = viewModelScope.launch {
        savePlaceUseCase(uri, place).collect { response ->
            when (response) {
                is Result.Success<*> -> {
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
