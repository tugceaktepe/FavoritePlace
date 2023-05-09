package com.aktepetugce.favoriteplace.location.domain.usecase

import android.net.Uri
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.util.StorageUtil
import javax.inject.Inject

class SavePlaceImage @Inject constructor(
    private val repository: PlaceRepository
) {
    operator fun invoke(imageId: String, imageUri: Uri) =
        repository.saveImage(StorageUtil.formatImagePath(imageId), imageUri)
}
