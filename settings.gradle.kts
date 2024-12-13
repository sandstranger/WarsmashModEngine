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
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven ("https://maven.nikr.net/")
        maven ("https://oss.sonatype.org/content/repositories/snapshots/")
        maven ("https://oss.sonatype.org/content/repositories/releases/")
        maven ("https://maven.nikr.net/")
        maven ("https://jitpack.io")
        maven ("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
        google()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven ("https://maven.nikr.net/")
        maven ("https://oss.sonatype.org/content/repositories/snapshots/")
        maven ("https://oss.sonatype.org/content/repositories/releases/")
        maven ("https://maven.nikr.net/")
        maven ("https://jitpack.io")
        maven ("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
        google()
    }
}

rootProject.name = "Warsmash-android"

include(":android")
include(":core")
include(":shared")
include(":fdfparser")
include(":jassparser")