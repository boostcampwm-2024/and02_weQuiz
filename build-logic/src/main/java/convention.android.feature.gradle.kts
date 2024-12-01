import kr.boostcamp_2024.course.build_logic.androidExtension
import kr.boostcamp_2024.course.build_logic.libs

plugins {
    id("convention.android.library")
    id("convention.android.compose")
    id("convention.android.hilt")
}

androidExtension.apply {
    pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

    dependencies {
        val libs = project.extensions.libs
        implementation(project(":core:domain"))
        implementation(project(":core:designsystem"))
        implementation(libs.findLibrary("androidx.navigation.compose").get())
        implementation(libs.findLibrary("kotlinx.serialization.json").get())
        implementation(libs.findLibrary("coil").get())
        implementation(libs.findLibrary("preferences.datastore").get())
        implementation(libs.findLibrary("mpandroidchart").get())
    }
}


