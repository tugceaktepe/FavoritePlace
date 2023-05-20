plugins {
    id("favoriteplace.android.library")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.android.hilt")
    id("favoriteplace.detekt")
    kotlin("kapt")
}
android {
    namespace  = "com.aktepetugce.favoriteplace.core"
}

dependencies{
    api(libs.androidx.core.ktx)

    api(libs.bundles.navigation)

    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.coroutines.android)

    api(libs.play.services.maps)
    api(libs.play.services.location)

    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.crashlytics)

    debugApi(libs.leak.canary)

    implementation(libs.glide.android)
    kapt(libs.glide.compiler)
}
