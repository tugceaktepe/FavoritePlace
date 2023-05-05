package com.aktepetugce.favoriteplace.location.ui.map

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.common.data.model.Place
import com.aktepetugce.favoriteplace.location.domain.DownloadImageUrl
import com.aktepetugce.favoriteplace.location.domain.SavePlaceDetail
import com.aktepetugce.favoriteplace.location.domain.SavePlaceImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val downloadImageUrlUseCase: DownloadImageUrl,
    private val savePlaceDetailUseCase: SavePlaceDetail,
    private val savePlaceImageUseCase: SavePlaceImage,

) : ViewModel() {

    private val _uiState = MutableStateFlow(MapsViewState())
    val uiState: StateFlow<MapsViewState> = _uiState

    fun savePlace(place: Place, uri: Uri) = viewModelScope.launch {
        val placeId = UUID.randomUUID().toString()
        _uiState.update { currentViewState ->
            currentViewState.copy(
                isLoading = true
            )
        }
        if (uri.toString() != "null") {
            savePlaceImageUseCase.invoke(placeId, uri).collect { response ->
                when (response) {
                    is com.aktepetugce.favoriteplace.common.model.Response.Success<*> -> {
                        downloadImageUrl(placeId, place)
                    }
                    is com.aktepetugce.favoriteplace.common.model.Response.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(errorMessage = response.message, isLoading = false)
                        }
                    }
                }
            }
        } else {
            savePlaceDetail(placeId, place)
        }
    }

    private fun downloadImageUrl(placeId: String, uiPlace: Place) = viewModelScope.launch {
        downloadImageUrlUseCase.invoke(placeId).collect { response ->
            when (response) {
                is com.aktepetugce.favoriteplace.common.model.Response.Success<*> -> {
                    val url = response.data as String?
                    uiPlace.imageUrl = url ?: ""
                    savePlaceDetail(placeId, uiPlace)
                }
                is com.aktepetugce.favoriteplace.common.model.Response.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = response.message, isLoading = false)
                    }
                }
            }
        }
    }

    private fun savePlaceDetail(placeId: String, uiPlace: Place) = viewModelScope.launch {
        savePlaceDetailUseCase.invoke(placeId, uiPlace).collect { response ->
            when (response) {
                is com.aktepetugce.favoriteplace.common.model.Response.Success<*> -> {
                    _uiState.update { currentState ->
                        currentState.copy(success = true, isLoading = false)
                    }
                }
                is com.aktepetugce.favoriteplace.common.model.Response.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = response.message, isLoading = false)
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
