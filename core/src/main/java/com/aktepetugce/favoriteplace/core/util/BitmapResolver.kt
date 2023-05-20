package com.aktepetugce.favoriteplace.core.util

import android.annotation.TargetApi
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.NonNull
import java.io.IOException

object BitmapResolver {
    private const val TAG = "BitmapResolver"
    private fun getBitmapLegacy(
        @NonNull contentResolver: ContentResolver,
        @NonNull fileUri: Uri
    ): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
        } catch (e: IOException) {
            Log.d(TAG, e.message ?: e.toString())
        }
        return bitmap
    }

    @TargetApi(Build.VERSION_CODES.P)
    private fun getBitmapImageDecoder(
        @NonNull contentResolver: ContentResolver,
        @NonNull fileUri: Uri
    ): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, fileUri))
        } catch (e: IOException) {
            Log.d(TAG, e.message ?: e.toString())
        }
        return bitmap
    }

    fun getBitmap(contentResolver: ContentResolver, fileUri: Uri?): Bitmap? {
        if (fileUri == null) {
            Log.i(TAG, "returning null because URI was null")
            return null
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getBitmapImageDecoder(contentResolver, fileUri)
        } else {
            getBitmapLegacy(contentResolver, fileUri)
        }
    }
}
