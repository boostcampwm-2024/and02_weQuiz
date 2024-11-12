package kr.boostcamp_2024.course.domain.model

data class Category(
    val title: String,
    val description: String?,
    val categoryImageUrl: String?,
    val quizzes: List<String>
)
