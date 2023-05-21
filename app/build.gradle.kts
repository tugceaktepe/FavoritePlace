
plugins {
    id("favoriteplace.android.application")
    id("favoriteplace.android.application.jacoco")
    id("favoriteplace.android.hilt")
    id("jacoco")
    id("favoriteplace.android.application.firebase")
    id("favoriteplace.detekt")
}

android {
    namespace = "com.aktepetugce.favoriteplace"

    defaultConfig {
        applicationId = "com.aktepetugce.favoriteplace"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner =
            "com.aktepetugce.favoriteplace.testing.CustomTestRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
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
    debugImplementation(libs.leak.canary)
}