package com.aktepetugce.favoriteplace.ui.map

data class MapsViewState(
    val nextDestination : Int? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

