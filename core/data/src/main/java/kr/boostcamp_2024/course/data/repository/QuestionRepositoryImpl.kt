package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.QuestionDTO
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : QuestionRepository {
    private val questionCollectionRef = firestore.collection("Question")

    override suspend fun getQuestion(questionId: String): Result<Question> =
        runCatching {
            val document = questionCollectionRef.document(questionId).get().await()
            val response = document.toObject(QuestionDTO::class.java)
            requireNotNull(response).toVO(questionId)
        }
}
