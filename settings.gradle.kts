pluginManagement {
    includeBuild("plugins")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "FavoritePlace"
include(":app")
include(":features:home")
include(":features:login")
include(":features:login")
include(":features:location")
include(":core")
include(":data")
include(":domain")
include(":testing")
include(":ui-components")
