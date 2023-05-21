package com.aktepetugce.favoriteplace.data.model

import kotlinx.serialization.SerialName

data class Place(
    val id: String? = "",
    val name: String? = "",
    val description: String? = "",
    val feeling: Int = 0,
    val imageUrl: String? = "",
    val latitude: String? = "",
    val longitude: String? = "",
    @SerialName("instance_id")
    val instanceId: Long? = 0L,
)
