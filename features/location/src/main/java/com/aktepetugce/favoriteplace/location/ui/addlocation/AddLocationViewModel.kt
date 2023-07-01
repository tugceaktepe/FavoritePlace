package com.aktepetugce.favoriteplace.location.ui.addlocation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<AddLocationUiState> =
        MutableStateFlow(AddLocationUiState())
    val uiState = _uiState.asStateFlow()

    fun saveSelectedPhoto(selectedPhotoUri: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedPhotoUri = selectedPhotoUri)
        }
    }

    fun getSelectedImageUri() = uiState.value.selectedPhotoUri
}
