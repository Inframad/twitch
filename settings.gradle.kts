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
include(":app")
include (":api")
include (":datasource")
include (":database")
include (":repository")
include (":model")
include(":common")
include(":streams")
include(":streams")
include(":navigation")
include(":favourite")
include(":notification")
include(":firebaseservice")
