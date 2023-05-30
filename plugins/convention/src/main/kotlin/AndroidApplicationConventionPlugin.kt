import com.aktepetugce.favoriteplace.configureKotlinAndroid
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.project

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 33

                val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
                configurations.configureEach {
                    resolutionStrategy {
                        force(libs.findLibrary("junit4").get())
                    }
                }

                dependencies {
                    add("implementation", project(":core"))
                    add("implementation", project(":ui-components"))
                    add("androidTestImplementation", project(":testing"))
                    add("testImplementation", project(":testing"))
                }
            }
        }
    }

}