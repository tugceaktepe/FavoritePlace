plugins {
    id("favoriteplace.android.feature")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.android.hilt")
    id("favoriteplace.detekt")
    alias(libs.plugins.safe.args)
}
android {
    namespace  = "com.aktepetugce.favoriteplace.home"
}

dependencies {
    implementation(libs.glide.android)
    implementation(libs.swipe.refresh.layout)
    kapt(libs.glide.compiler)
}