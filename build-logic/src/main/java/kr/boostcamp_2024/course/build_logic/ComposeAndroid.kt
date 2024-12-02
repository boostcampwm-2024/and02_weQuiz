package kr.boostcamp_2024.course.build_logic

import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.androidTestImplementation
import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.debugImplementation
import gradle.kotlin.dsl.accessors._2fb5859a04200edaf14b854c40b2e363.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureComposeAndroid() {
    with(pluginManager) {
        apply("org.jetbrains.kotlin.plugin.compose")
    }

    androidExtension.apply {
        buildFeatures.compose = true
    }

    dependencies {
        val libs = project.extensions.libs
        val bom = libs.findLibrary("androidx-compose-bom").get()
        implementation(platform(bom))
        androidTestImplementation(platform(bom))
        implementation(libs.findLibrary("androidx.material3").get())
        implementation(libs.findLibrary("androidx.material.icons.extended").get())
        implementation(libs.findLibrary("androidx.ui").get())
        implementation(libs.findLibrary("androidx.ui.tooling.preview").get())
        implementation(libs.findLibrary("androidx.ui.graphics").get())
        implementation(libs.findLibrary("coil.network.okhttp").get())
        implementation(libs.findLibrary("coil").get())
        androidTestImplementation(libs.findLibrary("androidx.espresso.core").get())
        debugImplementation(libs.findLibrary("androidx.ui.test.manifest").get())
        debugImplementation(libs.findLibrary("androidx.ui.tooling").get())
    }

    extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
        includeSourceInformation.set(true)
    }
}
