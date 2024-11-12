package kr.boostcamp_2024.course.domain.model

data class StudyGroup(
    val categories: List<String>,
    val description: String?,
    val maxUserNum: Int,
    val name: String,
    val ownerId: String,
    val studyGroupImageUrl: String?,
    val users: List<String>,
)
