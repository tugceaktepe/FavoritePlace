package com.aktepetugce.favoriteplace.core.di

import android.content.Context
import com.aktepetugce.favoriteplace.core.R
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        /*if (BuildConfig.DEBUG) {
            builder.setLogLevel(Log.VERBOSE)
        }*/
        builder.setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )
    }
}
