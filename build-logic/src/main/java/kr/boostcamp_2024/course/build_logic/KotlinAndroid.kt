package kr.boostcamp_2024.course.build_logic

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.FileInputStream
import java.util.Properties

internal fun Project.configureKotlinAndroid() {
    val localProperties = Properties().apply {
        load(FileInputStream(rootProject.file("local.properties")))
    }

    with(pluginManager) {
        apply("org.jetbrains.kotlin.android")
    }

    androidExtension.apply {
        compileSdk = 34

        defaultConfig {
            minSdk = 24
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
            isCoreLibraryDesugaringEnabled = true
        }

        buildTypes {
            getByName("debug") {
                buildConfigField(
                    "String",
                    "DEFAULT_USER_KEY",
                    localProperties.getProperty("DEFAULT_USER_KEY")
                )
            }

            getByName("release") {
                isMinifyEnabled = false
                buildConfigField(
                    "String",
                    "DEFAULT_USER_KEY",
                    localProperties.getProperty("DEFAULT_USER_KEY")
                )
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
        testOptions {
            unitTests {
                isIncludeAndroidResources = true
            }
        }
        buildFeatures {
            buildConfig = true
        }
    }

    configureKotlin()

    val libs = extensions.libs
    dependencies {
        add("coreLibraryDesugaring", libs.findLibrary("android.desugarJdkLibs").get())
    }
}

internal fun Project.configureKotlin() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
            // Treat all Kotlin warnings as errors (disabled by default)
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            freeCompilerArgs.set(
                freeCompilerArgs.get() + listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                )
            )
        }
    }
}