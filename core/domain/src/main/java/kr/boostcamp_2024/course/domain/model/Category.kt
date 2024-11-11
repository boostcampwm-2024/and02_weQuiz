package kr.boostcamp_2024.course.domain.model

data class Category(
    val title: String,
    val description: String?,
    val categoryImage: String?,
    val quizzes: List<String>
)
