pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
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

rootProject.name = "BasicConverter"

include(":app")
include(":core:core-database")
include(":core:core-domain")
include(":core:core-presentation")
include(":core:core-common")
include(":core:core-network")
include(":core:core-navigation")

include(":feature:feature-converter")
include(":feature:feature-settings")
include(":core:core-data")
include(":feature:feature-settings-api")
