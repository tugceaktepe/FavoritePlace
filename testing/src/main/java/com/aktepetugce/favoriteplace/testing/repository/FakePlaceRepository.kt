package com.aktepetugce.favoriteplace.testing.repository

import android.net.Uri
import com.aktepetugce.favoriteplace.data.model.Place
import com.aktepetugce.favoriteplace.data.repository.PlaceRepository
import com.aktepetugce.favoriteplace.testing.data.places
import com.aktepetugce.favoriteplace.testing.util.constant.CommonConstants.API_CALL_TIME
import com.aktepetugce.favoriteplace.testing.util.constant.LoginConstants
import com.aktepetugce.favoriteplace.testing.util.constant.PlaceConstants.IMAGE_URL
import kotlinx.coroutines.delay
import javax.inject.Inject

class FakePlaceRepository @Inject constructor() : PlaceRepository {

    override suspend fun saveImage(imagePath: String, imageUri: Uri): Boolean {
        delay(API_CALL_TIME)
        if (imageUri.toString() == "") {
            throw NullPointerException("Invalid image")
        }
        return true
    }

    @Suppress("UseRequire")
    override suspend fun downloadImageUrl(imagePath: String): String {
        delay(API_CALL_TIME)
        if (imagePath.contains("6")) {
            throw java.lang.IllegalArgumentException("Invalid image path")
        }
        return IMAGE_URL
    }

    @Suppress("UseCheckOrError")
    override suspend fun savePlaceDetail(email: String, place: Place) {
        delay(API_CALL_TIME)
        if (email.isEmpty()) {
            throw java.lang.IllegalStateException("Authorization error")
        }
    }

    override suspend fun fetchPlaces(userEmail: String): List<Place> {
        delay(API_CALL_TIME)
        if (userEmail != LoginConstants.TEST_EMAIL) {
            throw IllegalArgumentException(LoginConstants.SIGN_IN_ERROR)
        }
        return places
    }
}
