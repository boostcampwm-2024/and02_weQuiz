plugins {
    id("convention.android.application")
}

android {
    namespace = "kr.boostcamp_2024.course.wequiz"

    defaultConfig {
        applicationId = "kr.boostcamp_2024.course.wequiz"
        targetSdk = 34
        versionCode = 3
        versionName = "1.1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(project(":feature:login"))
    implementation(project(":feature:main"))
    implementation(project(":feature:study"))
    implementation(project(":feature:category"))
    implementation(project(":feature:quiz"))

    implementation(project(":core:data"))
    implementation(project(":core:domain"))
    implementation(project(":core:designsystem"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.ui.test.junit4)
}
