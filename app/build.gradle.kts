@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
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
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.3")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    implementation(libs.google.material){
        exclude("androidx.activity","activity")
        exclude("androidx.appcompat","appcompat")
        exclude("androidx.constraintlayout","constraintlayout")
        exclude("androidx.core","core")
        exclude("androidx.recyclerview","recyclerview")
    }
    implementation(libs.bundles.kotlins)

    //compose
    val composeBomVersion ="2024.01.00"

    val composeBom = platform("androidx.compose:compose-bom:${composeBomVersion}")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Choose one of the following:
    // Material Design 3
    implementation("androidx.compose.material3:material3:1.2.0")
    // Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    // UI Tests
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
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
    //test
//    androidTestImplementation(platform("androidx.compose:compose-bom:${composeBomVersion}"))

    implementation(composeLibs.google.accompanist.systemUiController)

    //datastore
    implementation(libs.bundles.dataStore) {
        exclude("org.jetbrains.kotlinx","kotlinx-coroutines-core")
    }

    implementation(others.github.mmkv)

    //lib
    implementation(project(":preference-ui-compose"))
    implementation(project(":preference-data-core"))
    implementation(project(":preference-mmkv-util"))
    implementation(project(":preference-util"))
    implementation(project(":datastore-util"))

}