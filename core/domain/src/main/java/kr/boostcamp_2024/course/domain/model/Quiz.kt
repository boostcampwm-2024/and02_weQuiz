package kr.boostcamp_2024.course.domain.model

import java.time.LocalDate

data class Quiz(
    val id: String,
    val title: String,
    val description: String?,
    val startTime: String,
    val solveTime: Int,
    val questions: List<String>,
    val userOmrs: List<String>,
    val quizImageUrl: String?,
) {
    val isOpened: Boolean
        get() = LocalDate.now().isAfter(LocalDate.parse(startTime))
}
