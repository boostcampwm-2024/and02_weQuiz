package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.QuestionDTO
import kr.boostcamp_2024.course.data.model.toDTO
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.QuestionCreationInfo
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : QuestionRepository {
    private val questionCollectionRef = firestore.collection("Question")

    override suspend fun getQuestions(questionIds: List<String>): Result<List<Question>> =
        runCatching {
            questionIds.map { questionId ->
                val document = questionCollectionRef.document(questionId).get().await()
                val response = document.toObject(QuestionDTO::class.java)
                requireNotNull(response).toVO(questionId)
            }
        }

    override suspend fun getQuestion(questionId: String): Result<Question> = runCatching {
        val document = questionCollectionRef.document(questionId).get().await()
        val response = document.toObject(QuestionDTO::class.java)
        requireNotNull(response).toVO(questionId)
    }

    override suspend fun createQuestion(questionCreationInfo: QuestionCreationInfo): Result<String> =
        runCatching {
            val document = questionCollectionRef.add(questionCreationInfo.toDTO()).await()
            document.id
        }

    override suspend fun deleteQuestions(questionIds: List<String>): Result<Unit> = runCatching {
        questionIds.forEach { questionId ->
            questionCollectionRef.document(questionId).delete().await()
        }
    }

}
