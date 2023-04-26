package com.aktepetugce.favoriteplace.domain.usecase.place

data class PlaceUseCases(
    val savePlaceImage: SavePlaceImage,
    val downloadImageUrl: DownloadImageUrl,
    val savePlaceDetail: SavePlaceDetail,
    val fetchPlaces: FetchPlaces
)
