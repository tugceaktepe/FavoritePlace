package com.aktepetugce.favoriteplace.domain.usecase.place

import com.aktepetugce.favoriteplace.data.repo.PlaceRepository

class DownloadImageUrl (
    private val repository: PlaceRepository
){
    operator fun invoke(imagePath: String) =
        repository.downloadImageUrl(imagePath)
}