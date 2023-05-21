plugins {
    id("favoriteplace.android.feature")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.detekt")
    alias(libs.plugins.safe.args)
}
android {
    namespace  = "com.aktepetugce.favoriteplace.home"
}

dependencies {
    implementation(libs.glide.android)
    kapt(libs.glide.compiler)
    implementation(libs.swipe.refresh.layout)
}