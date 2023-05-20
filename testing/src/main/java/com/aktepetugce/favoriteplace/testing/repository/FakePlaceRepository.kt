package com.aktepetugce.favoriteplace.testing.repository

import android.net.Uri
import com.aktepetugce.favoriteplace.data.model.Place
import com.aktepetugce.favoriteplace.data.repository.PlaceRepository
import javax.inject.Inject

class FakePlaceRepository @Inject constructor() : PlaceRepository {

    override suspend fun saveImage(imagePath: String, imageUri: Uri): Boolean {
        return true
    }

    override suspend fun downloadImageUrl(imagePath: String): String {
        return ""
    }

    override suspend fun savePlaceDetail(email: String, place: Place) {
        TODO("Not yet implemented")
    }

    override suspend fun fetchPlaces(userEmail: String): List<Place> {
        TODO("Not yet implemented")
    }
}
