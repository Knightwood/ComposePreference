import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")

}

android {
    namespace = "com.kiylx.libx.pref_component.datastore_util"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.kotlin.coroutines.core)
    compileOnly(project(":preference-data-core"))
    //datastore
    implementation(libs.androidx.datastore.preferences) {
        exclude("org.jetbrains.kotlinx","kotlinx-coroutines-core")
    }
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.knightwood"
                artifactId = "datastore-util"
                version = rootProject.ext["version"].toString()
                afterEvaluate {
                    from(components["release"])
                }
                pom {
                    name.set("datastore-util")
                    description.set("preference datastore util")
                    url.set("https://github.com/Knightwood/ComposePreference")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("knightwood")
                            name.set("KnightWood")
                            email.set("33772264+Knightwood@users.noreply.github.com")
                        }
                    }
                }
            }
        }
    }
}