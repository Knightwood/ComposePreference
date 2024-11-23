plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.kiylx.compose.preference"
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
    implementation(libs.github.knightwood.m3ColorUtilities)
    implementation(libs.kotlin.coroutines.core)
    implementation(platform(composeLibs.androidx.compose.bom))
    implementation(composeLibs.androidx.compose.material3)
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

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
                pom {
                    name.set("preference-ui-compose")
                    description.set("A compose ui for preference")
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
