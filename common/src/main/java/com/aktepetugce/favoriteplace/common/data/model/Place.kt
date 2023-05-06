package com.aktepetugce.favoriteplace.common.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class Place(
    @SerialName("user_email")
    var userEmail: String? = "",
    var name: String? = "",
    var type: String? = "",
    var atmosphere: String? = "",
    var imageUrl: String? = "",
    var latitude: String? = "",
    var longitude: String? = "",
    @SerialName("instance_id")
    var instanceId: Long? = 0L
) : Parcelable
