import kr.boostcamp_2024.course.build_logic.androidExtension
import kr.boostcamp_2024.course.build_logic.configureComposeAndroid
import kr.boostcamp_2024.course.build_logic.configureFirebase
import kr.boostcamp_2024.course.build_logic.configureHiltAndroid
import kr.boostcamp_2024.course.build_logic.configureKotlinAndroid
import kr.boostcamp_2024.course.build_logic.libs
import org.gradle.kotlin.dsl.dependencies

plugins {
    id("com.android.application")
}

androidExtension.apply {
    pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

    dependencies {
        val libs = project.extensions.libs
        add("implementation", libs.findLibrary("androidx.navigation.compose").get())
        add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
    }
}

configureComposeAndroid()
configureKotlinAndroid()
configureHiltAndroid()