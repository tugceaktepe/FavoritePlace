import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("io.gitlab.arturbosch.detekt")
                withPlugin("io.gitlab.arturbosch.detekt") {
                    val rootProject = target.rootProject

                    target.extensions.configure<DetektExtension> {
                        buildUponDefaultConfig = true
                        baseline = target.file("config/detekt-baseline.xml")
                        basePath = rootProject.projectDir.absolutePath

                        val localDetektConfig = target.file("detekt.yml")
                        val rootDetektConfig =
                            target.rootProject.file("config/detekt.yml")
                        if (localDetektConfig.exists()) {
                            config.from(localDetektConfig, rootDetektConfig)
                        } else {
                            config.from(rootDetektConfig)
                        }
                    }
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                "detektPlugins"(libs.findLibrary("detekt.formatting").get())
            }
        }
    }

}