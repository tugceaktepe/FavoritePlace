plugins {
    id("favoriteplace.android.library")
    id("favoriteplace.android.library.jacoco")
    id("favoriteplace.android.hilt")
    id("favoriteplace.detekt")
}

android {
    namespace = "com.aktepetugce.favoriteplace.testing"
}

dependencies {
    api(project(":data"))
    implementation(project(":core"))
    implementation(project(":ui-components"))

    api(libs.androidx.test.core)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.ext)
    api(libs.hilt.android.testing)
    api(libs.androidx.test.espresso.core)
    api(libs.testing.androidx.junit)
    api(libs.androidx.test.espresso.resource.idling)
    api(libs.androidx.test.espresso.contrib) {
        exclude("com.google.protobuf")
    }

    debugApi(libs.androidx.fragment.testing)
    api(libs.androidx.navigation.testing)

    api(libs.kotlinx.coroutines.test)
}