package com.aktepetugce.favoriteplace.ui.home

import com.aktepetugce.favoriteplace.domain.model.UIPlace

data class HomeViewState(
    val isUserAuthenticated: Boolean = false,
    val email: String = "",
    val placeList: List<UIPlace>? = listOf(),
    val placesLoaded: Boolean = false,
    val signOutSuccess: Boolean = false,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)
