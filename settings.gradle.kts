pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }

}
rootProject.name = "TwitchApp"
include(
    ":app",
    ":api",
    ":datasource",
    ":database",
    ":repository",
    ":model",
    ":common",
    ":features:streams",
    ":features:favourite",
    ":features:notification",
    ":features:notificationservice",
    ":features:appreview"
)