package kr.boostcamp_2024.course.build_logic

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFirebase() {
    with(pluginManager) {
        apply("com.google.gms.google-services")
    }

    dependencies {
        val libs = project.extensions.libs
        val bom = libs.findLibrary("firebase-bom").get()
        add("implementation", platform(bom))
        add("implementation", libs.findLibrary("firebase.auth").get())
        add("implementation", libs.findLibrary("firebase.firestore").get())
        add("implementation", libs.findLibrary("firebase.storage").get())
    }

}
