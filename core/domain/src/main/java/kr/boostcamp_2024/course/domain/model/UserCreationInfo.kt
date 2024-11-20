package kr.boostcamp_2024.course.domain.model

data class UserCreationInfo(
    val email: String,
    val name: String,
    val profileImageUrl: String?,
)
