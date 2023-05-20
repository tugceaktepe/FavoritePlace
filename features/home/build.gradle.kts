plugins {
    id("favoriteplace.android.feature")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.android.hilt")
    id("favoriteplace.detekt")
    alias(libs.plugins.safe.args)
    kotlin("kapt")
}
android {
    namespace  = "com.aktepetugce.favoriteplace.home"
}

dependencies {
    implementation(libs.glide.android)
    kapt(libs.glide.compiler)
    implementation(libs.swipe.refresh.layout)
}