package kr.boostcamp_2024.course.domain.model

data class UserCreationInfo(
    val email: String,
    val password: String,
    val nickName: String,
    val profileImage: String?,
)
