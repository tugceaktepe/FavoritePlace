package com.aktepetugce.favoriteplace.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Place(
    @SerialName("user_email")
    var userEmail : String? = "",
    var name: String? = "",
    var type: String? = "",
    var atmosphere: String? = "",
    var imageUrl : String? = "",
    var latitude: String? = "",
    var longitude: String? = "",
    @SerialName("instance_id")
    var instanceId: Long? = 0L
)