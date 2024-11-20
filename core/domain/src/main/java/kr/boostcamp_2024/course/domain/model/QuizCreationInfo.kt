package kr.boostcamp_2024.course.domain.model

data class QuizCreationInfo(
    val quizImageUrl: String?,
    val quizTitle: String,
    val quizDescription: String?,
    val quizDate: String,
    val quizSolveTime: Int,
)
