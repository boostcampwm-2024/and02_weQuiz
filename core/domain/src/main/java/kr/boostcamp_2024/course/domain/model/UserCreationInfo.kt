package kr.boostcamp_2024.course.domain.model

data class UserCreationInfo(
    val email: String,
    val nickName: String,
    val profileImageUrl: String?,
    val studyGroups: List<String> = emptyList(),
)
