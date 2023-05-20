plugins {
    id("favoriteplace.android.library")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.android.hilt")
    id("favoriteplace.detekt")
}

android {
    namespace = "com.aktepetugce.favoriteplace.uicomponents"
}
dependencies {
    api(libs.androidx.appcompat)
    api(libs.constraint.layout)
    api(libs.material)
}