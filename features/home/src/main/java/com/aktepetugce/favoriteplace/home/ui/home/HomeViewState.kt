package com.aktepetugce.favoriteplace.home.ui.home

import com.aktepetugce.favoriteplace.common.domain.model.Place

data class HomeViewState(
    val placeList: List<Place>? = listOf(),
    val placesLoaded: Boolean = false,
    val signOutSuccess: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)
