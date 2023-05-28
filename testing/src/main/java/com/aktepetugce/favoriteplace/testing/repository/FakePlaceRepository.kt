package com.aktepetugce.favoriteplace.testing.repository

import android.net.Uri
import com.aktepetugce.favoriteplace.data.model.Place
import com.aktepetugce.favoriteplace.data.repository.PlaceRepository
import com.aktepetugce.favoriteplace.testing.data.places
import com.aktepetugce.favoriteplace.testing.util.constant.CommonConstants.API_CALL_TIME
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants
import kotlinx.coroutines.delay
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
        delay(API_CALL_TIME)
        if (userEmail != LoginConstants.TEST_EMAIL) {
            throw IllegalArgumentException(LoginConstants.SIGN_IN_ERROR)
        }
        return places
    }
}
