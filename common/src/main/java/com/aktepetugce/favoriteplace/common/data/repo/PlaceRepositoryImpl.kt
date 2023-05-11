package com.aktepetugce.favoriteplace.common.data.repo

import android.net.Uri
import com.aktepetugce.favoriteplace.common.data.model.Place
import com.aktepetugce.favoriteplace.common.extension.toResult
import com.aktepetugce.favoriteplace.common.model.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val storageReference: StorageReference,
    private val dispatcher: CoroutineDispatcher,
) : PlaceRepository {
    override suspend fun saveImage(imagePath: String, imageUri: Uri): Boolean =
        withContext(dispatcher) {
            val storageReferenceChild = this@PlaceRepositoryImpl.storageReference.child(imagePath)
            val taskResult = storageReferenceChild.putFile(imageUri).await()
            return@withContext taskResult.task.isSuccessful
        }

    override suspend fun downloadImageUrl(imagePath: String): String = withContext(dispatcher) {
        val imageReference = storage.getReference(imagePath)
        val url = imageReference.downloadUrl.await()
        return@withContext url?.toString() ?: ""
    }

    override fun savePlaceDetail(email: String, place: Place) = flow<Result<*>> {
        val documentRef = firestore.collection("places")
            .document("$email")
            .collection("myplaces")
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
        emit(Result.Success(true))
    }.flowOn(dispatcher)

    override fun fetchPlaces(email: String) = flow {
        val reference = firestore.collection("places")
            .document(email)
            .collection("myplaces")
        val places = mutableListOf<Place>()
        try {
            reference.get().await().mapNotNull { snapShot ->
                val place = snapShot.toObject(Place::class.java)
                places.add(place)
            }
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: exception.toString()))
        }
        emit(places)

    }.flowOn(dispatcher)
        .toResult()
}
