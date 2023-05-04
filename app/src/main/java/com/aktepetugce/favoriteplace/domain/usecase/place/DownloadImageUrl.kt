package com.aktepetugce.favoriteplace.domain.usecase.place

import com.aktepetugce.favoriteplace.common.util.StorageUtil
import com.aktepetugce.favoriteplace.data.repo.PlaceRepository

class DownloadImageUrl(
    private val repository: PlaceRepository
) {
    operator fun invoke(imageId: String) =
        repository.downloadImageUrl(StorageUtil.formatImagePath(imageId))
}
