plugins {
    id("favoriteplace.android.feature")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.android.hilt")
    alias(libs.plugins.safe.args)
}
android {
    namespace  = "com.aktepetugce.favoriteplace.home"
}

dependencies {
    implementation(libs.glide.android)
    kapt(libs.glide.compiler)
}