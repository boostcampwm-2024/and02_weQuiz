plugins {
    id("convention.android.library")
    id("convention.firebase")
    id("convention.android.hilt")
}

android {
    namespace = "kr.boostcamp_2024.course.data"
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
}
