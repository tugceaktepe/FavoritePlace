package com.aktepetugce.favoriteplace.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aktepetugce.favoriteplace.domain.usecase.authentication.AuthUseCases
import com.aktepetugce.favoriteplace.domain.usecase.place.PlaceUseCases
import com.aktepetugce.favoriteplace.base.BaseViewModel
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace
import com.aktepetugce.favoriteplace.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val placeUseCases : PlaceUseCases,
    private val authUseCases: AuthUseCases
) : BaseViewModel() {

    private val _isSignOutSuccess = MutableLiveData<Boolean>()
    val isSignOutSuccess: LiveData<Boolean> = _isSignOutSuccess

    fun signOut() = viewModelScope.launch {
        authUseCases.signOut.invoke().collect { response ->
            when (response) {
                is Response.Success<*> -> {
                    _isLoading.value= false
                    _isSignOutSuccess.value = true
                }
                is Response.Error -> {
                    _isLoading.value= false
                    _error.value = response.message
                }
                else -> _isLoading.value = true
            }
        }
    }

    private val _placesList = MutableLiveData<List<UIPlace>>()
    val placesList: LiveData<List<UIPlace>> = _placesList

    fun fetchPlaces(userEmail: String) = viewModelScope.launch {
        _isLoading.value = true
        placeUseCases.fetchPlaces(userEmail).collect { response ->
            when(response) {
                is Response.Success<*> -> {
                    _isLoading.value = false
                    _placesList.value = response.data as List<UIPlace>?
                }
                is Response.Error -> {
                    _isLoading.value = false
                    _error.value = response.message
                }
            }
        }
    }
    val currentUserEmail get() = authUseCases.currentUserEmail.invoke()
}