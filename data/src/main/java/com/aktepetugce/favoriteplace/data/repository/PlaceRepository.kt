package com.aktepetugce.favoriteplace.data.repository

import android.net.Uri
import com.aktepetugce.favoriteplace.data.model.Place

interface PlaceRepository {
    suspend fun saveImage(imagePath: String, imageUri: Uri): Boolean
    suspend fun downloadImageUrl(imagePath: String): String
    suspend fun savePlaceDetail(email: String, place: Place)
    suspend fun fetchPlaces(userEmail: String): List<Place>
}
