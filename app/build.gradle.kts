@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.kiylx.composepreference"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kiylx.composepreference"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            applicationIdSuffix = ".r1"
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
        }
    }
}

dependencies {
    implementation(libs.bundles.bundleAndroidx)
    implementation(libs.bundles.kotlins)

    val composeBom = platform(composeLibs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(composeLibs.androidx.compose.material3)

    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    //icons
    implementation("androidx.compose.material:material-icons-extended")
    // Optional - Add window size utils
    implementation("androidx.compose.material3:material3-window-size-class")
    // Optional - Integration with activities
    implementation(composeLibs.androidx.activity.compose)
    // Optional - Integration with ViewModels
    implementation(composeLibs.androidx.lifecycle.viewmodel.compose)
    // Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation(composeLibs.google.accompanist.systemUiController)

    //datastore
    implementation(libs.androidx.datastore.preferences) {
        exclude("org.jetbrains.kotlinx","kotlinx-coroutines-core")
    }

    implementation(libs.github.mmkv)

    //lib
    implementation(project(":preference-ui-compose"))
    implementation(project(":preference-data-core"))
    implementation(project(":preference-mmkv-util"))
    implementation(project(":preference-util"))
    implementation(project(":datastore-util"))

}