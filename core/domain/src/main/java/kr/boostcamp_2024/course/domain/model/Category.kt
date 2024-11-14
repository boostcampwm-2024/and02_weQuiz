package kr.boostcamp_2024.course.domain.model

data class Category(
    val id: String,
    val name: String,
    val description: String?,
    val categoryImageUrl: String?,
    val quizzes: List<String>,
)
