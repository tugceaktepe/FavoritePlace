
plugins {
    id("favoriteplace.android.application")
    id("favoriteplace.android.application.jacoco")
    id("favoriteplace.android.hilt")
    id("jacoco")
    id("favoriteplace.android.application.firebase")
}

android {
    namespace = "com.aktepetugce.favoriteplace"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.aktepetugce.favoriteplace"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":features:login"))
    implementation(project(":features:location"))
    implementation(project(":features:home"))

    implementation(libs.bundles.navigation)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
}