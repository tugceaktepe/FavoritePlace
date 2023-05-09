package com.aktepetugce.favoriteplace.common.data.repo

import android.net.Uri
import com.aktepetugce.favoriteplace.common.data.model.Place
import com.aktepetugce.favoriteplace.common.model.Response
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val storageReference: StorageReference,
    private val dispatcher: CoroutineDispatcher,
) : PlaceRepository {

    override fun savePlaceDetail(email: String, place: Place) = flow<Response<*>> {
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
        emit(Response.Success(true))
    }.flowOn(dispatcher)
        .catch { emit(Response.Error(it.message ?: it.toString())) }

    override fun saveImage(imagePath: String, imageUri: Uri) = flow<Response<*>> {
        val storageReferenceChild = this@PlaceRepositoryImpl.storageReference.child(imagePath)
        storageReferenceChild.putFile(imageUri).await()
        emit(Response.Success(true))
    }.flowOn(dispatcher)
        .catch { emit(Response.Error(it.message ?: it.toString())) }

    override fun downloadImageUrl(imagePath: String) = flow<Response<*>> {
        val imageReference = storage.getReference(imagePath)
        val url = imageReference.downloadUrl.await()
        if (url != null) {
            emit(Response.Success(url.toString()))
        } else {
            emit(Response.Error("Download Error"))
        }
    }.flowOn(dispatcher)
        .catch { emit(Response.Error(it.message ?: it.toString())) }

    override fun fetchPlaces(email: String) = flow<Response<*>> {
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
            emit(Response.Error(exception.message ?: exception.toString()))
        }
        emit(Response.Success(places))

    }.flowOn(dispatcher)
        .catch { emit(Response.Error(it.message ?: it.toString())) }
        .onStart { emit(Response.Loading) }
}
