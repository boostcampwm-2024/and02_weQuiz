package kr.boostcamp_2024.course.data.model

data class UserOmrDTO(
    val userId: String = "",
    val quizId: String = "",
    val answers: List<Int> = emptyList(),
)

