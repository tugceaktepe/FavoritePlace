plugins {
    id("favoriteplace.android.library")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.detekt")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.aktepetugce.favoriteplace.domain"
}

dependencies {
    implementation(project(":core"))
    implementation(project(":data"))
}