package com.aktepetugce.favoriteplace.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace
import com.aktepetugce.favoriteplace.domain.usecase.place.PlaceUseCases
import com.aktepetugce.favoriteplace.util.Response
import com.aktepetugce.favoriteplace.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapsViewModel  @Inject constructor(
    private val placeUseCases: PlaceUseCases
) : ViewModel() {

    val error = SingleLiveEvent<String>()
    val isLoading = SingleLiveEvent<Boolean>()
    val isSaveSuccess = SingleLiveEvent<Boolean>()

    fun savePlace(place: UIPlace) = viewModelScope.launch {
        //TODO: Move this action to domain layer
        val placeId  = UUID.randomUUID().toString()
        isLoading.value = true
        if(place.placeImage != null){
            placeUseCases.savePlaceImage(placeId,place.placeImage!!).collect { response ->
                when (response) {
                    is Response.Success<*> -> {
                        downloadImageUrl(placeId, place)
                    }
                    is Response.Error -> {
                        isLoading.value = false
                        error.value = response.message
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
                    isLoading.value = false
                    error.value = response.message
                }
            }
        }
    }

    private fun savePlaceDetail(placeId : String, uiPlace: UIPlace) = viewModelScope.launch {
        placeUseCases.savePlaceDetail(placeId,uiPlace).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    isLoading.value=false
                    isSaveSuccess.value = true
                }
                is Response.Error -> {
                    isLoading.value = false
                    error.value = response.message
                }
            }
        }
    }

}