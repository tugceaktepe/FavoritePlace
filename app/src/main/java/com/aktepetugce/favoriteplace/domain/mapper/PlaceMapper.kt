package com.aktepetugce.favoriteplace.domain.mapper

import com.aktepetugce.favoriteplace.data.model.Place
import com.aktepetugce.favoriteplace.domain.uimodel.UIPlace

class PlaceMapper {
    fun map(model: Place?): UIPlace? {
        return model?.toUIPlace()
    }

    private fun Place.toUIPlace() = UIPlace(
        placeUserEmail = this.userEmail ?: "",
        placeName = this.name ?: "",
        placeImage = null ,
        placeType = this.type ?: "",
        placeAtmosphere =  this.atmosphere ?: "",
        placeImageUrl = this.imageUrl ?: "",
        placeLatitude = this.latitude?.toDouble() ?: 0.0 ,
        placeLongitude = this.longitude?.toDouble() ?: 0.0
    )
}