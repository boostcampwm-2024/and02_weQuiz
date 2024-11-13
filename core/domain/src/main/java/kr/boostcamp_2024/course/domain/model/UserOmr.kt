package kr.boostcamp_2024.course.domain.model

data class UserOmr(
    val userId: String,
    val quizId: String,
    val answers: List<Int>,
    )

