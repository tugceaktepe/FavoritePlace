package com.aktepetugce.favoriteplace.data.repo

import android.net.Uri
import com.aktepetugce.favoriteplace.data.model.Place
import com.aktepetugce.favoriteplace.util.Response
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class PlaceRepositoryImpl @Inject constructor(
    private val database : FirebaseDatabase,
    private val databaseReference: DatabaseReference,
    private val storage: FirebaseStorage,
    private var storageReference : StorageReference): PlaceRepository {

    override fun savePlaceDetail(placeId : String, place: Place) = flow {
        try {

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
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override fun saveImage(imagePath: String, imageUri: Uri) = flow {
        try {
            storageReference = storageReference.child(imagePath)
            storageReference.putFile(imageUri).await()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override fun downloadImageUrl(imagePath: String) = flow {
        try {
            val imageReference = storage.getReference(imagePath)
            val url = imageReference.downloadUrl.await()
            if(url != null){
                emit(Response.Success(url.toString()))
            }else{
                emit(Response.Error("Download Error"))
            }
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override fun fetchPlaces(userEmail : String) = callbackFlow {
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
    }
}