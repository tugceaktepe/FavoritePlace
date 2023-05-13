package com.aktepetugce.favoriteplace.home.ui.home

import com.aktepetugce.favoriteplace.common.domain.model.Place

sealed class HomeUiState {
    object InitialState : HomeUiState()
    object Loading : HomeUiState()
    data class PlaceListLoaded(
        val placeList: List<Place> = listOf(),
    ) : HomeUiState() {
        fun isEmpty(): Boolean = placeList.isEmpty()
    }

    object UserSignedOut : HomeUiState()
    data class Error(val message: String, val isNotShown: Boolean) : HomeUiState()
}
