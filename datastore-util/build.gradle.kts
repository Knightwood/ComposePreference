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
    implementation(libs.bundles.dataStore) {
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
            }
        }
    }
}