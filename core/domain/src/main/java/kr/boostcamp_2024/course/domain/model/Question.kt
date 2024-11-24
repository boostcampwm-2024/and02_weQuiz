package kr.boostcamp_2024.course.domain.model

data class Question(
    val id: String,
    val title: String,
    val description: String,
    val solution: String?,
    val answer: Int,
    val choices: List<String>,
    val userAnswers: List<Int>,
)
