package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(categoryIds: List<String>): Result<List<Category>>

    suspend fun getCategory(categoryId: String): Result<Category>

    suspend fun createCategory(categoryName: String, categoryDescription: String?): Result<String>

    suspend fun addQuiz(categoryId: String, quizId: String): Result<Unit>

    suspend fun addQuizToCategory(categoryId: String, quizId: String): Result<Unit>

    suspend fun deleteCategories(categoryIds: List<String>): Result<Unit>
}
