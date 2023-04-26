package com.aktepetugce.favoriteplace.domain.usecase.place

import android.net.Uri
import com.aktepetugce.favoriteplace.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.util.StorageUtil

class SavePlaceImage(
    private val repository: PlaceRepository
) {
    operator fun invoke(imageId: String, imageUri: Uri) =
        repository.saveImage(StorageUtil.formatImagePath(imageId), imageUri)
}
