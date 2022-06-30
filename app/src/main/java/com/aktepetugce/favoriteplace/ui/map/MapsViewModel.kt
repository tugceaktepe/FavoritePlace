package com.aktepetugce.favoriteplace.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace
import com.aktepetugce.favoriteplace.domain.usecase.place.PlaceUseCases
import com.aktepetugce.favoriteplace.base.BaseViewModel
import com.aktepetugce.favoriteplace.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MapsViewModel  @Inject constructor(
    private val placeUseCases: PlaceUseCases
) : BaseViewModel() {

    private val _isSaveSuccess = MutableLiveData<Boolean>()
    val  isSaveSuccess : LiveData<Boolean> = _isSaveSuccess

    fun savePlace(place: UIPlace) = viewModelScope.launch {
        val placeId  = UUID.randomUUID().toString()
        _isLoading.value = true
        if(place.placeImage != null){
            placeUseCases.savePlaceImage.invoke(getImagePath(placeId),place.placeImage!!).collect { response ->
                when (response) {
                    is Response.Success<*> -> {
                        downloadImageUrl(placeId, place)
                    }
                    is Response.Error -> {
                        _isLoading.value = false
                        _error.value = response.message
                    }
                }
            }
        }else{
            savePlaceDetail(placeId,place)
        }
    }

    private fun downloadImageUrl(placeId : String, uiPlace: UIPlace) = viewModelScope.launch {
        placeUseCases.downloadImageUrl.invoke(getImagePath(placeId)).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    val url = response.data as String?
                    uiPlace.placeImageUrl = url ?: ""
                    savePlaceDetail(placeId,uiPlace)
                }
                is Response.Error -> {
                     _isLoading.value = false
                    _error.value = response.message
                }
            }
        }
    }

    private fun savePlaceDetail(placeId : String, uiPlace: UIPlace) = viewModelScope.launch {
        placeUseCases.savePlaceDetail.invoke(placeId,uiPlace).collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    _isLoading.value=false
                    _isSaveSuccess.value = true
                }
                is Response.Error -> {
                    _isLoading.value = false
                    _error.value = response.message
                }
            }
        }
    }

    private fun getImagePath(placeId: String) : String{
        return "images/$placeId.jpg"
    }

}