package kr.boostcamp_2024.course.build_logic

import gradle.kotlin.dsl.accessors._4b055a01bae563bd2c86a468691a3401.implementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFirebase() {
    with(pluginManager) {
        apply("com.google.gms.google-services")
    }

    dependencies {
        val libs = project.extensions.libs
        val bom = libs.findLibrary("firebase-bom").get()
        implementation(platform(bom))
        implementation(libs.findLibrary("firebase.auth").get())
        implementation(libs.findLibrary("firebase.firestore").get())
        implementation(libs.findLibrary("firebase.storage").get())
    }
}
