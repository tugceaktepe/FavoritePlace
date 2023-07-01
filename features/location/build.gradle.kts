plugins {
    id("favoriteplace.android.feature")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.detekt")
    alias(libs.plugins.safe.args)
}
android {
    namespace  = "com.aktepetugce.favoriteplace.location"
}

dependencies {
    implementation(libs.glide.android)
    kapt(libs.glide.compiler)
}