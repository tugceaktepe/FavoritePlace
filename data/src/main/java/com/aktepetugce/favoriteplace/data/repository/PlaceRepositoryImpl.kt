package com.aktepetugce.favoriteplace.data.repository

import android.net.Uri
import com.aktepetugce.favoriteplace.core.annotation.IoDispatcher
import com.aktepetugce.favoriteplace.data.model.Place
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : PlaceRepository {
    override suspend fun saveImage(imagePath: String, imageUri: Uri): Boolean =
        withContext(dispatcher) {
            val storageReferenceChild = this@PlaceRepositoryImpl.storage.reference.child(imagePath)
            val taskResult = storageReferenceChild.putFile(imageUri).await()
            taskResult.task.isSuccessful
        }

    override suspend fun downloadImageUrl(imagePath: String): String = withContext(dispatcher) {
        val imageReference = storage.getReference(imagePath)
        val url = imageReference.downloadUrl.await()
        url?.toString() ?: ""
    }

    override suspend fun savePlaceDetail(email: String, place: Place): Unit =
        withContext(dispatcher) {
            val documentRef = firestore.collection(PLACES)
                .document("$email")
                .collection(PERSONAL_PLACES)
                .document("${place.id}")
            val placeData = hashMapOf(
                "id" to place.id,
                "name" to place.name,
                "description" to place.description,
                "imageUrl" to place.imageUrl,
                "longitude" to place.longitude,
                "latitude" to place.latitude,
                "feeling" to place.feeling,
                "instance_id" to place.instanceId
            )
            documentRef.set(placeData)
        }

    override suspend fun fetchPlaces(userEmail: String): List<Place> = withContext(dispatcher) {
        val reference = firestore.collection(PLACES)
            .document(userEmail)
            .collection(PERSONAL_PLACES)
        val places = mutableListOf<Place>()
        reference.get().await().mapNotNull { snapShot ->
            val place = snapShot.toObject(Place::class.java)
            places.add(place)
        }
        places.toList()
    }

    companion object {
        const val PLACES = "places"
        const val PERSONAL_PLACES = "myplaces"
    }
}
