plugins {
    id("convention.android.feature")
}

android {
    namespace = "kr.boostcamp_2024.course.login"
}

dependencies {
    implementation(libs.credentails)
    implementation(libs.credentails.play.services.auth)
    implementation(libs.google.id)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}
