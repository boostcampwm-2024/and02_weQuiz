package kr.boostcamp_2024.course.build_logic

import gradle.kotlin.dsl.accessors._4b055a01bae563bd2c86a468691a3401.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureHiltAndroid() {
    with(pluginManager) {
        apply("dagger.hilt.android.plugin")
        apply("com.google.devtools.ksp")
    }

    dependencies {
        val libs = project.extensions.libs
        implementation(libs.findLibrary("hilt.android").get())
        implementation(libs.findLibrary("hilt.navigation.compose").get())
        "ksp"(libs.findLibrary("hilt.android.compiler").get())
    }
}
