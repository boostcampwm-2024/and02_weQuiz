package kr.boostcamp_2024.course.domain.model

data class Quiz(
    val title: String,
    val description: String?,
    val startTime: String,
    val solveTime: Int,
    val questions: List<String>,
    val userOmrs: List<String>,
)
