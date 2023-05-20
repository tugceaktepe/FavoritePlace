package com.aktepetugce.favoriteplace.location.ui.map

sealed class MapsUiState {
    object InitialState : MapsUiState()
    object Loading : MapsUiState()
    object LocationIsAdded : MapsUiState()
    data class Error(val message: String) : MapsUiState()
}
