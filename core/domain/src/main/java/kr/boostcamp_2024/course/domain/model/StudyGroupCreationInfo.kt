package kr.boostcamp_2024.course.domain.model

data class StudyGroupCreationInfo(
    val description: String?,
    val maxUserNum: Int,
    val name: String,
    val ownerId: String
)
