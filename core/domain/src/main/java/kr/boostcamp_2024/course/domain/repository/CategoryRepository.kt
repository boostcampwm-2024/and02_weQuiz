package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(categoryIds: List<String>): List<Category>

    suspend fun getCategory(categoryId: String): Category

    suspend fun createCategory(categoryName: String, categoryDescription: String?, categoryImageUrl: String?): String

    suspend fun addQuiz(categoryId: String, quizId: String)

    suspend fun addQuizToCategory(categoryId: String, quizId: String)

    suspend fun deleteQuizFromCategory(categoryId: String, quizId: String)

    suspend fun deleteCategories(categoryIds: List<String>)

    suspend fun deleteCategory(categoryId: String)

    suspend fun updateCategory(categoryId: String, categoryName: String, categoryDescription: String?, categoryImageUrl: String?)
}
