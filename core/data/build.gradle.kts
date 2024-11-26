import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    id("convention.android.library")
    id("convention.firebase")
    id("convention.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}
var properties = Properties()
properties.load(FileInputStream("local.properties"))
android {
    namespace = "kr.boostcamp_2024.course.data"
    defaultConfig {
        buildConfigField("String", "BASE_URL", properties.getProperty("base_url"))
        buildConfigField("String", "POST_URL", properties.getProperty("post_url"))
        buildConfigField("String", "X_NCP_CLOVASTUDIO_API_KEY", properties.getProperty("X-NCP-CLOVASTUDIO-API-KEY"))
        buildConfigField("String", "X_NCP_APIGW_API_KEY", properties.getProperty("X-NCP-APIGW-API-KEY"))
        buildConfigField("String", "X_NCP_CLOVASTUDIO_REQUEST_ID", properties.getProperty("X-NCP-CLOVASTUDIO-REQUEST-ID"))
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:domain"))
    implementation(libs.preferences.datastore)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.kotlinx.coroutines)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.converter.kotlinx.serialization)
}
