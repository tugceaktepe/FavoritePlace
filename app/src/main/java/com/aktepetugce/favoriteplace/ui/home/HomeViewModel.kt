package com.aktepetugce.favoriteplace.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace
import com.aktepetugce.favoriteplace.domain.usecase.authentication.AuthUseCases
import com.aktepetugce.favoriteplace.domain.usecase.place.PlaceUseCases
import com.aktepetugce.favoriteplace.util.Response
import com.aktepetugce.favoriteplace.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val placeUseCases : PlaceUseCases,
    private val authUseCases: AuthUseCases
) : ViewModel() {

    val error = SingleLiveEvent<String>()
    val isLoading = SingleLiveEvent<Boolean>()
    val isSignOutSuccess = SingleLiveEvent<Boolean>()
    
    fun signOut() = viewModelScope.launch {
        authUseCases.signOut().collect { response ->
            when(response) {
                is Response.Success<*> -> {
                    isLoading.value= false
                    isSignOutSuccess.value = true
                }
                is Response.Error -> {
                    isLoading.value= false
                    error.value = response.message
                }
                else -> isLoading.value = true
            }
        }
    }

    private val _placesList = MutableLiveData<List<UIPlace>>(listOf())
    val placesList  : LiveData<List<UIPlace>> = _placesList

    fun fetchPlaces(userEmail: String) = viewModelScope.launch {
        isLoading.value = true
        placeUseCases.fetchPlaces(userEmail).collect { response ->
            when(response) {
                is Response.Success<*> -> {
                    isLoading.value = false
                    response.data.let {
                        _placesList.value = it as List<UIPlace>
                    }
                }
                is Response.Error -> {
                    isLoading.value = false
                    error.value = response.message
                }
            }
        }
    }

    val currentUserEmail get() = authUseCases.currentUserEmail.invoke()
}