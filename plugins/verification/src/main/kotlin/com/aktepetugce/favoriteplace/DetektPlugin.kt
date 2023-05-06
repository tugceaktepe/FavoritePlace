import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class DetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.plugins.apply("io.gitlab.arturbosch.detekt")
        target.plugins.withId("io.gitlab.arturbosch.detekt") {
            val rootProject = target.rootProject

            target.extensions.configure<DetektExtension> {
                buildUponDefaultConfig = true
                baseline = target.file("../config/detekt/detekt-baseline.xml")
                basePath = rootProject.projectDir.absolutePath

                val localDetektConfig = target.file("detekt.yml")
                val rootDetektConfig = target.rootProject.file("../config/detekt/detekt.yml")
                if (localDetektConfig.exists()) {
                    config.from(localDetektConfig, rootDetektConfig)
                } else {
                    config.from(rootDetektConfig)
                }
            }
        }
    }

}