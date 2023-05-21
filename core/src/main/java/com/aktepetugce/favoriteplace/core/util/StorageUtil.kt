package com.aktepetugce.favoriteplace.core.util

object StorageUtil {
    fun formatImagePath(imageId: String): String {
        return "images/$imageId.jpg"
    }
}
