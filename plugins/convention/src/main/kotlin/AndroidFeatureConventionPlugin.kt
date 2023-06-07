import com.aktepetugce.favoriteplace.configureKotlinAndroid
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("favoriteplace.android.library")
                apply("favoriteplace.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 33
                defaultConfig {
                    testInstrumentationRunner = "com.aktepetugce.favoriteplace.testing.CustomTestRunner"
                }
                buildFeatures.viewBinding = true
                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }
                    packaging {
                        resources.excludes.addAll(
                            listOf(
                                "META-INF/LICENSE.md",
                                "META-INF/LICENSE-notice.md",
                            )
                        )
                    }
                }
            }

            dependencies {
                add("implementation", project(":domain"))
                add("implementation", project(":core"))
                add("implementation", project(":ui-components"))
                add("testImplementation", kotlin("test"))
                add("testImplementation", project(":testing"))
                add("androidTestImplementation", kotlin("test"))
                add("androidTestImplementation", project(":testing"))
            }
        }
    }
}
