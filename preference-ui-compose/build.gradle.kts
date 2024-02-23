plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "com.kiylx.compose_lib.pref_component"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false //true会在打包后因为没有引用而导致class.jar为空
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
        }
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(others.github.knightwood.m3ColorUtilities)
    implementation(libs.kotlin.coroutines.core)
    //compose
    val composeBom = platform(composeLibs.androidx.compose.bom)
    implementation(composeBom)
    // Material Design 3
    implementation(composeLibs.androidx.compose.material3)
    //icons
//    implementation("androidx.compose.material:material-icons-extended")
//    implementation("androidx.compose.material:material-icons-core")
    compileOnly(project(":preference-data-core"))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = "com.github.knightwood"
                artifactId = "preference-ui-compose"
                version = rootProject.ext["version"].toString()
                afterEvaluate {
                    from(components["release"])
                }
            }
        }
    }
}
