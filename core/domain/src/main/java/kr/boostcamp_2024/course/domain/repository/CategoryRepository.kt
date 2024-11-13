package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(categoryIds: List<String>): Result<List<Category>>

    suspend fun getCategory(categoryId: String): Result<Category>
}
