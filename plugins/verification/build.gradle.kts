plugins {
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.plugin.detekt)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

gradlePlugin {
    plugins {
        register("detektPlugin") {
            id = "favoriteplace.detekt"
            implementationClass = "DetektPlugin"
        }
    }
}