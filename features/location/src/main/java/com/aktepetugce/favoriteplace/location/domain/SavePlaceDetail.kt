package com.aktepetugce.favoriteplace.location.domain

import com.aktepetugce.favoriteplace.common.data.model.Place
import com.aktepetugce.favoriteplace.common.data.repo.PlaceRepository
import javax.inject.Inject

class SavePlaceDetail @Inject constructor(
    private val repository: PlaceRepository
) {
    operator fun invoke(placeId: String, uiPlace: Place) =
        repository.savePlaceDetail(placeId, uiPlace)
}
