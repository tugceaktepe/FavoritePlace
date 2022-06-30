package com.aktepetugce.favoriteplace.domain.usecase.place

import android.net.Uri
import com.aktepetugce.favoriteplace.data.repo.PlaceRepository

class SavePlaceImage (
    private val repository: PlaceRepository
){
    operator fun invoke(imagePath: String, imageUri : Uri) =
        repository.saveImage(imagePath, imageUri)
}