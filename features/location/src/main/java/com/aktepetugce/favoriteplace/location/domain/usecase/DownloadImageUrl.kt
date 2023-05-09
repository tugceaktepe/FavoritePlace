package com.aktepetugce.favoriteplace.location.domain.usecase

import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.common.util.StorageUtil
import javax.inject.Inject

class DownloadImageUrl @Inject constructor(
    private val repository: PlaceRepository
) {
    operator fun invoke(imageId: String) =
        repository.downloadImageUrl(StorageUtil.formatImagePath(imageId))
}
