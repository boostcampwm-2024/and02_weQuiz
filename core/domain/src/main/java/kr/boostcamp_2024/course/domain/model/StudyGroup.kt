package kr.boostcamp_2024.course.domain.model

data class StudyGroup(
    val id: String,
    val name: String,
    val studyGroupImageUrl: String?,
    val description: String?,
    val maxUserNum: Int,
    val ownerId: String,
    val users: List<String>,
    val categories: List<String>,
)