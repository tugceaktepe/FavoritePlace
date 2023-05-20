package com.aktepetugce.favoriteplace.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class Place(
    val id: String,
    val name: String,
    val description: String,
    val feeling: Pair<Int, Int>,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    @SerialName("instance_id")
    val instanceId: Long
) : Parcelable