package kr.boostcamp_2024.course.domain.model

data class UserOmr(
    val id: String,
    val userId: String,
    val quizId: String,
    val answers: List<Any>,
)
