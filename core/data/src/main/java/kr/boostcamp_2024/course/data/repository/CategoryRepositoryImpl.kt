package kr.boostcamp_2024.course.data.repository

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

    override suspend fun getCategory(categoryId: String): Result<Category> =
        runCatching {
            val document = categoryCollectionRef.document(categoryId).get().await()
            val response = document.toObject(CategoryDTO::class.java)
            requireNotNull(response).toVO(categoryId)
        }

    override suspend fun getCategories(categoryIds: List<String>): Result<List<Category>> =
        runCatching {
            categoryIds.map { categoryId ->
                val document = categoryCollectionRef.document(categoryId).get().await()
                val response = document.toObject(CategoryDTO::class.java)
                requireNotNull(response).toVO(categoryId)
            }
        }
}
