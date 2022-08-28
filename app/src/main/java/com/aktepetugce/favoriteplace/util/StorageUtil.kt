package com.aktepetugce.favoriteplace.util

object StorageUtil {
    fun formatImagePath(imageId: String) : String{
        return "images/$imageId.jpg"
    }
}