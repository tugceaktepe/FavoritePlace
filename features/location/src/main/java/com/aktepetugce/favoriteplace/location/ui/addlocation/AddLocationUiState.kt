package com.aktepetugce.favoriteplace.location.ui.addlocation

import android.net.Uri

data class AddLocationUiState(
    val locationName: String,
    val selectedPhotoUri: Uri? = null,
)
