package com.aktepetugce.favoriteplace.domain.uimodel

import android.net.Uri
import android.os.Parcelable
import com.aktepetugce.favoriteplace.data.model.Place
import kotlinx.parcelize.Parcelize


@Parcelize
data class UIPlace(
                   var placeUserEmail: String,
                   var placeName: String,
                   var placeImage: Uri?,
                   var placeType: String,
                   var placeAtmosphere: String,
                   var placeImageUrl : String,
                   var placeLatitude: Double,
                   var placeLongitude: Double) : Parcelable {
}

fun UIPlace.toPlace() = Place(
    userEmail = placeUserEmail,
    name = placeName,
    type = placeType,
    atmosphere = placeAtmosphere,
    imageUrl = placeImageUrl,
    latitude = placeLatitude.toString(),
    longitude = placeLongitude.toString()
)