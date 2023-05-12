package com.aktepetugce.favoriteplace.location.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MapsArgs(
    val name: String,
    val uri: String
) : Parcelable
