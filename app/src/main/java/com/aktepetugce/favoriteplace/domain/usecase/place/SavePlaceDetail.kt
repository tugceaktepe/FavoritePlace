package com.aktepetugce.favoriteplace.domain.usecase.place

import com.aktepetugce.favoriteplace.data.repo.PlaceRepository
import com.aktepetugce.favoriteplace.domain.model.UIPlace
import com.aktepetugce.favoriteplace.domain.model.toPlace

class SavePlaceDetail(
    private val repository: PlaceRepository
) {
    operator fun invoke(placeId: String, uiPlace: UIPlace) =
        repository.savePlaceDetail(placeId, uiPlace.toPlace())
}
