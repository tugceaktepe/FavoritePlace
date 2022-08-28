package com.aktepetugce.favoriteplace.data.repo

import android.net.Uri
import com.aktepetugce.favoriteplace.data.model.Place
import com.aktepetugce.favoriteplace.util.Response
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class PlaceRepositoryImpl @Inject constructor(
    private val database: FirebaseDatabase,
    private val databaseReference: DatabaseReference,
    private val storage: FirebaseStorage,
    private val storageReference: StorageReference,
    private val dispatcher: CoroutineDispatcher
) : PlaceRepository {

    override fun savePlaceDetail(placeId: String, place: Place) = flow<Response<*>> {
        databaseReference.child("Places")
            .child(placeId)
            .child("user_email")
            .setValue(place.userEmail)

        databaseReference.child("Places")
            .child(placeId)
            .child("name")
            .setValue(place.name)

        databaseReference.child("Places")
            .child(placeId)
            .child("type")
            .setValue(place.type)

        databaseReference.child("Places")
            .child(placeId)
            .child("atmosphere")
            .setValue(place.atmosphere)

        databaseReference.child("Places")
            .child(placeId)
            .child("imageUrl")
            .setValue(place.imageUrl)

        databaseReference.child("Places")
            .child(placeId)
            .child("latitude")
            .setValue(place.latitude)

        databaseReference.child("Places")
            .child(placeId)
            .child("longitude")
            .setValue(place.longitude)

        databaseReference.child("Places")
            .child(placeId)
            .child("instance_id")
            .setValue(System.currentTimeMillis())
        emit(Response.Success(true))
    }.catch {
        emit(Response.Error(it.message ?: it.toString()))
    }.flowOn(dispatcher)

    override fun saveImage(imagePath: String, imageUri: Uri) = flow<Response<*>>{
        val storageReferenceChild = this@PlaceRepositoryImpl.storageReference.child(imagePath)
        storageReferenceChild.putFile(imageUri).await()
        emit(Response.Success(true))
    }.catch {
        emit(Response.Error(it.message ?: it.toString()))
    }.flowOn(dispatcher)

    override fun downloadImageUrl(imagePath: String) = flow<Response<*>> {
        val imageReference = storage.getReference(imagePath)
        val url = imageReference.downloadUrl.await()
        if (url != null) {
            emit(Response.Success(url.toString()))
        } else {
            emit(Response.Error("Download Error"))
        }
    }.catch {
        emit(Response.Error(it.message ?: it.toString()))
    }.flowOn(dispatcher)

    override fun fetchPlaces(userEmail: String) = callbackFlow {
         val postListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                this@callbackFlow.trySendBlocking(Response.Error(error.message))
                channel.close()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = dataSnapshot.children.map { ds ->
                    ds.getValue(Place::class.java)
                }
                this@callbackFlow.trySendBlocking(Response.Success(items.filterNotNull()))
                channel.close()
            }
        }
        val dbReference = database.getReference("Places")
        val query = dbReference.orderByChild("user_email").equalTo(userEmail)
        query.addValueEventListener(postListener)

        awaitClose {
            query.removeEventListener(postListener)
        }
    }.catch {
        emit(Response.Error(it.message ?: it.toString()))
    }.flowOn(dispatcher)
}