plugins {
    id("convention.android.feature")
}

android {
    namespace = "kr.boostcamp_2024.course.quiz"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}