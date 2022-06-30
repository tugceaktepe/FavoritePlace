package com.aktepetugce.favoriteplace.data.repo

import android.net.Uri
import com.aktepetugce.favoriteplace.data.model.Place
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
   fun saveImage(imagePath : String, imageUri: Uri): Flow<Any>
   fun downloadImageUrl(imagePath : String): Flow<Any>
   fun savePlaceDetail(placeId : String, place: Place): Flow<Any>
   fun fetchPlaces(userEmail : String) : Flow<Any>
}