package com.aktepetugce.favoriteplace.common.data.repo

import android.net.Uri
import com.aktepetugce.favoriteplace.common.data.model.Place
import com.aktepetugce.favoriteplace.common.model.Result
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    suspend fun saveImage(imagePath: String, imageUri: Uri): Boolean
    suspend fun downloadImageUrl(imagePath: String): String
    fun savePlaceDetail(email: String, place: Place): Flow<Result<*>>
    fun fetchPlaces(userEmail: String): Flow<Any>
}
