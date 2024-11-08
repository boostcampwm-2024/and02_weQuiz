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
        add("implementation", project(":core:domain"))
        add("implementation", project(":core:designsystem"))
        add("implementation", libs.findLibrary("androidx.navigation.compose").get())
        add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
        add("implementation",libs.findLibrary("coil").get())
    }
}


