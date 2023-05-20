package com.aktepetugce.favoriteplace.domain.model.args

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MapsArgs(
    val name: String,
    val uri: String
) : Parcelable
