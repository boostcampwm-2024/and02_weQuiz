package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.CategoryDTO
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : CategoryRepository {
    private val categoryCollectionRef = firestore.collection("Category")

    override suspend fun getCategories(categoryIds: List<String>): Result<List<Category>> =
        runCatching {
            categoryIds.map { categoryId ->
                val document = categoryCollectionRef.document(categoryId).get().await()
                val response = document.toObject(CategoryDTO::class.java)
                requireNotNull(response).toVO(categoryId)
            }
        }

    override suspend fun getCategory(categoryId: String): Result<Category> = runCatching {
        val document = categoryCollectionRef.document(categoryId).get().await()
        val response = document.toObject(CategoryDTO::class.java)
        requireNotNull(response).toVO(categoryId)
    }

    override suspend fun createCategory(
        categoryName: String,
        categoryDescription: String?,
        categoryImageUrl: String?,
    ): Result<String> = runCatching {
        val newCategory = CategoryDTO(
            name = categoryName,
            description = categoryDescription,
            categoryImageUrl = categoryImageUrl,
            quizzes = emptyList(),
        )

        val document = categoryCollectionRef.add(newCategory).await()
        document.id
    }

    override suspend fun addQuiz(categoryId: String, quizId: String): Result<Unit> =
        runCatching {
            categoryCollectionRef.document(categoryId).update("quizzes", FieldValue.arrayUnion(quizId)).await()
        }

    override suspend fun addQuizToCategory(categoryId: String, quizId: String): Result<Unit> =
        runCatching {
            val document = categoryCollectionRef.document(categoryId)
            document.update("quizzes", FieldValue.arrayUnion(quizId)).await()

        }

    override suspend fun deleteQuizFromCategory(categoryId: String, quizId: String): Result<Unit> =
        runCatching {
            val document = categoryCollectionRef.document(categoryId)
            document.update("quizzes", FieldValue.arrayRemove(quizId)).await()

        }

    override suspend fun deleteCategories(categoryIds: List<String>): Result<Unit> =
        runCatching {
            categoryIds.forEach { categoryId ->
                categoryCollectionRef.document(categoryId).delete().await()
            }
        }

    override suspend fun deleteCategory(categoryId: String): Result<Unit> =
        kotlin.runCatching {
            categoryCollectionRef.document(categoryId).delete().await()
        }

    override suspend fun updateCategory(
        categoryId: String,
        categoryName: String,
        categoryDescription: String?,
        categoryImageUrl: String?,
    ): Result<Unit> = runCatching {
        categoryCollectionRef.document(categoryId).update(
            mapOf(
                "name" to categoryName,
                "description" to categoryDescription,
                "category_image_url" to categoryImageUrl,
            ),
        ).await()
    }
}
