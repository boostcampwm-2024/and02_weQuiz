package kr.boostcamp_2024.course.build_logic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureHiltAndroid() {
    with(pluginManager) {
        apply("dagger.hilt.android.plugin")
        apply("com.google.devtools.ksp")
    }

    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("hilt.android").get())
        "implementation"(libs.findLibrary("hilt.navigation.compose").get())
        "kspAndroidTest"(libs.findLibrary("hilt.android.compiler").get())
        "ksp"(libs.findLibrary("hilt.android.compiler").get())
    }
}
