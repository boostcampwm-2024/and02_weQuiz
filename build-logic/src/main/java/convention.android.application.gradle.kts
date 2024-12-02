import kr.boostcamp_2024.course.build_logic.androidExtension
import kr.boostcamp_2024.course.build_logic.configureKotlinAndroid
import kr.boostcamp_2024.course.build_logic.libs

plugins {
    id("com.android.application")
    id("convention.android.compose")
    id("convention.android.hilt")
    id("convention.firebase")
}

configureKotlinAndroid()

androidExtension.apply {
    pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

    dependencies {
        val libs = project.extensions.libs
        implementation(libs.findLibrary("androidx.navigation.compose").get())
        implementation(libs.findLibrary("kotlinx.serialization.json").get())
    }
}
