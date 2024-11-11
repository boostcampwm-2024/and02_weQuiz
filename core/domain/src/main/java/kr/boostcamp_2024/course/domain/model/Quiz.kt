package kr.boostcamp_2024.course.domain.model

data class Quiz(
    val title: String,
    val description: String,
    var startTime: String,
    var solveTime: Int,
    val questions: List<String>,
    var userOmrs: List<String>,
)
