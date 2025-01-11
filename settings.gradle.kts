pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        gradlePluginPortal()
    }
    plugins {
        id("com.google.dagger.hilt.android") version "2.55"
        id("com.google.devtools.ksp") version "2.1.0-1.0.29"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "IntroDistributedProjectApp"
include(":app")
 