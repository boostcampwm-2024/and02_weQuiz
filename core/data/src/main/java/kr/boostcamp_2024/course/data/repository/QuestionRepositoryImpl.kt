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
    private val firestore: FirebaseFirestore,
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

    override suspend fun updateCurrentSubmit(questionId: String, selectedIndex: Int): Result<Unit> =
        runCatching {
            val document = questionCollectionRef.document(questionId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(document)

                if (snapshot.exists()) {
                    val userAnswers = snapshot.get("user_answers") as? MutableList<Int> ?: throw Exception("user_answers 배열이 존재하지 않거나 잘못되었습니다.")

                    if (selectedIndex < 0 || selectedIndex >= userAnswers.size) {
                        throw Exception("잘못된 인덱스입니다.")
                    }

                    userAnswers[selectedIndex] += 1

                    transaction.update(document, "user_answers", userAnswers)
                } else {
                    throw Exception("문서가 존재하지 않습니다.")
                }
            }.await()
        }
}
