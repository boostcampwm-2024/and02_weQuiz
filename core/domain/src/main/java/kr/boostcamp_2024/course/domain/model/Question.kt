package kr.boostcamp_2024.course.domain.model

data class Question(
    val title: String = "",
    val description: String = "",
    val choices: List<String> = emptyList(),
    val solution: String = "",
    val answer: Int = -1,
)
