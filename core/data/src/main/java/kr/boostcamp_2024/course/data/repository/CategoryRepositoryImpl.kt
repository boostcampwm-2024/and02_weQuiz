package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : CategoryRepository {
    private val categoryCollectionRef = firestore.collection("Category")
}
