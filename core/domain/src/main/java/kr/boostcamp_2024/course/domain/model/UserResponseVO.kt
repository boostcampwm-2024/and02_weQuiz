package kr.boostcamp_2024.course.domain.model

data class UserResponseVO(
    val email: String,
    val name: String,
    val profileUrl: String?,
    val studyGroups: List<String>
)
