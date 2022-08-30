package com.aktepetugce.favoriteplace.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.R
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace
import com.aktepetugce.favoriteplace.domain.usecase.place.PlaceUseCases
import com.aktepetugce.favoriteplace.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapsViewModel  @Inject constructor(
    private val placeUseCases: PlaceUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapsViewState())
    val uiState: StateFlow<MapsViewState> = _uiState

    fun savePlace(place: UIPlace) = viewModelScope.launch {
        //TODO: Move this action to domain layer
        val placeId  = UUID.randomUUID().toString()
        _uiState.update { currentViewState ->
            currentViewState.copy(
                isLoading = true
            )
        }
        if(place.placeImage != null){
            placeUseCases.savePlaceImage(placeId,place.placeImage!!).collect { response ->
                when (response) {
                    is Response.Success<*> -> {
                        downloadImageUrl(placeId, place)
                    }
                    is Response.Error -> {
                        _uiState.update { currentState ->
                            currentState.copy(errorMessage = response.message, isLoading = false)
                        }
                    }
                }
            }
        }else{
            savePlaceDetail(placeId,place)
        }
    }

    private fun downloadImageUrl(placeId : String, uiPlace: UIPlace) = viewModelScope.launch {
        placeUseCases.downloadImageUrl(placeId).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    val url = response.data as String?
                    uiPlace.placeImageUrl = url ?: ""
                    savePlaceDetail(placeId,uiPlace)
                }
                is Response.Error -> {
                    _uiState.update { currentState ->
                        currentState.copy(errorMessage = response.message, isLoading = false)
                    }
                }
            }
        }
    }

    private fun savePlaceDetail(placeId : String, uiPlace: UIPlace) = viewModelScope.launch {
        placeUseCases.savePlaceDetail(placeId,uiPlace).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    _uiState.update { currentState ->
                        currentState.copy(nextDestination = R.id.action_maps_to_home, isLoading = false)
                    }
                }
                is Response.Error -> {
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