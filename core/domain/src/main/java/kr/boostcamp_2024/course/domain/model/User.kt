package kr.boostcamp_2024.course.domain.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val profileUrl: String?,
    val studyGroups: List<String>,
)
