plugins {
    id("favoriteplace.android.library")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.detekt")
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.aktepetugce.favoriteplace.domain"
}

dependencies {
    implementation(project(":data"))
    implementation(project(":core"))
}