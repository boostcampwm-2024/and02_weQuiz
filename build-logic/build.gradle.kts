plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.android.desugarJdkLibs)
    implementation(libs.kotlin.gradlePlugin)
}