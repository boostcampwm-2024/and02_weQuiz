package kr.boostcamp_2024.course.domain.model

data class UserSubmitInfo(
    val email: String,
    val name: String,
    val profileImageUrl: String?,
    val studyGroups: List<String> = emptyList(),
)
