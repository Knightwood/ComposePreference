import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("java-library")
    kotlin("jvm")
    id("maven-publish")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation(libs.kotlin.coroutines.core)
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = "com.github.knightwood"
            artifactId = "preference-data-core"
            version = rootProject.ext["version"].toString()
            from(components.findByName("java"))

            pom {
                name.set("preference-data-core")
                description.set("preference data core")
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

//private fun Project.configureKotlin() {
//    // Use withType to workaround https://youtrack.jetbrains.com/issue/KT-55947
//    tasks.withType<KotlinCompile>().configureEach {
//        kotlinOptions {
//            // Set JVM target to 11
//            jvmTarget = JavaVersion.VERSION_11.toString()
//            // Treat all Kotlin warnings as errors (disabled by default)
//            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
//            val warningsAsErrors: String? by project
//            allWarningsAsErrors = warningsAsErrors.toBoolean()
//            freeCompilerArgs = freeCompilerArgs + listOf(
//                // Enable experimental coroutines APIs, including Flow
//                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
//            )
//        }
//    }
//}