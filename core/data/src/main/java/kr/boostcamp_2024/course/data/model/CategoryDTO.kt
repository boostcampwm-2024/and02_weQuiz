package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.Category

data class CategoryDTO(
    val name: String? = null,
    val description: String? = null,
    @get:PropertyName("category_image_url")
    @set:PropertyName("category_image_url")
    var categoryImageUrl: String? = null,
    val quizzes: List<String>? = null,
) {
    fun toVO(categoryId: String) = Category(
        id = categoryId,
        name = requireNotNull(name),
        description = description,
        categoryImageUrl = categoryImageUrl,
        quizzes = requireNotNull(quizzes),
    )
}
