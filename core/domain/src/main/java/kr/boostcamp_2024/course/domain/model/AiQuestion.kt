package kr.boostcamp_2024.course.domain.model

data class AiQuestion(
    val answer: String,
    val choices: List<String>,
    val description: String,
    val title: String,
    val solution: String,
)
