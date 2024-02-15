pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://www.jitpack.io")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
    }
    versionCatalogs {
        create("composeLibs") {
            from(files("./gradle/composeLibs.versions.toml"))
        }
        create("others") {
            from(files("./gradle/others.versions.toml"))
        }
    }
}
rootProject.name = "ComposePreference"
include(":app")
include(":compose_lib")
