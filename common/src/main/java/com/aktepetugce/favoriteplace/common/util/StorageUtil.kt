package com.aktepetugce.favoriteplace.common.util

object StorageUtil {
    fun formatImagePath(imageId: String): String {
        return "images/$imageId.jpg"
    }
}
