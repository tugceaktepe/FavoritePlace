plugins {
    id("favoriteplace.android.library")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.android.hilt")
    id("favoriteplace.detekt")
}

android {
    namespace = "com.aktepetugce.favoriteplace.data"
}

dependencies {
    implementation(project(":core"))

    implementation(libs.firebase.database)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore)
}