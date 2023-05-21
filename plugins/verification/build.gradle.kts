plugins {
    `kotlin-dsl`
}

group = "com.aktepetugce.favoriteplace.verification"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.plugin.detekt)
}

gradlePlugin {
    plugins {
        register("detektPlugin") {
            id = "favoriteplace.detekt"
            implementationClass = "DetektPlugin"
        }
    }
}