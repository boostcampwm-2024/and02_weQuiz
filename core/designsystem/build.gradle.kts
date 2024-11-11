plugins {
	id("convention.android.library")
	id("convention.android.compose")
}

android {
	namespace = "kr.boostcamp_2024.course.designsystem"
}

dependencies {
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
}
