plugins {
    id("favoriteplace.android.library")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.android.hilt")
    id("favoriteplace.detekt")
    kotlin("kapt")
}
android {
    namespace  = "com.aktepetugce.favoriteplace.common"
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.constraint.layout)
    api(libs.material)

    api(libs.bundles.navigation)

    api(libs.kotlinx.serialization.json)
    api(libs.kotlinx.coroutines.android)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.glide.android)
    kapt(libs.glide.compiler)

    api(libs.play.services.maps)

    api(platform(libs.firebase.bom))
    api(libs.firebase.analytics)
    api(libs.firebase.crashlytics)
    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
}