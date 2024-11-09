package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
): QuestionRepository {
    private val questionCollectionRef = firestore.collection("Question")
}