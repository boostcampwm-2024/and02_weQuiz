package kr.boostcamp_2024.course.domain.model

import java.time.LocalDate

data class Quiz(
    override val id: String,
    override val title: String,
    override val description: String?,
    val startTime: String,
    val solveTime: Int,
    override val questions: List<String>,
    override val userOmrs: List<String>,
) : BaseQuiz() {
    val isOpened: Boolean
        get() = LocalDate.now().isAfter(LocalDate.parse(startTime))
}
