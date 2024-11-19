package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.QuizDTO
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.model.QuizCreationInfo
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : QuizRepository {
    private val quizCollectionRef = firestore.collection("Quiz")

    override suspend fun addQuestionToQuiz(quizId: String, questionId: String): Result<Unit> =
        runCatching {
            val document = quizCollectionRef.document(quizId)
            document.update("questions", FieldValue.arrayUnion(questionId)).await()
        }

    override suspend fun createQuiz(quizCreateInfo: QuizCreationInfo): Result<String> =
        runCatching {
            val newQuiz = QuizDTO(
                title = quizCreateInfo.quizTitle,
                description = quizCreateInfo.quizDescription,
                startTime = quizCreateInfo.quizDate,
                solveTime = quizCreateInfo.quizSolveTime,
                questions = emptyList(),
                userOmrs = emptyList(),
                quizImageUrl = quizCreateInfo.quizImageUrl,
            )
            val document = quizCollectionRef.add(newQuiz).await()
            document.id
        }

    override suspend fun getQuiz(quizId: String): Result<Quiz> =
        runCatching {
            val document = quizCollectionRef.document(quizId).get().await()
            val response = document.toObject(QuizDTO::class.java)
            requireNotNull(response).toVO(quizId)
        }

    override suspend fun getQuizList(quizIdList: List<String>): Result<List<Quiz>> =
        runCatching {
            quizIdList.map { quizId ->
                val document = quizCollectionRef.document(quizId).get().await()
                val response = document.toObject(QuizDTO::class.java)
                requireNotNull(response).toVO(quizId)
            }
        }

    override suspend fun addUserOmrToQuiz(quizId: String, userOmrId: String): Result<Unit> =
        runCatching {
            quizCollectionRef.document(quizId)
                .update("user_omrs", FieldValue.arrayUnion(userOmrId))
                .await()
        }

    override suspend fun editQuiz(quizId: String, quizCreateInfo: QuizCreationInfo): Result<Unit> =
        runCatching {
            val updatedData = mapOf(
                "title" to quizCreateInfo.quizTitle,
                "description" to quizCreateInfo.quizDescription,
                "start_time" to quizCreateInfo.quizDate,
                "solve_time" to quizCreateInfo.quizSolveTime,
                "quiz_image_url" to quizCreateInfo.quizImageUrl,
            )
            quizCollectionRef.document(quizId)
                .update(updatedData)
                .await()
        }

    override suspend fun deleteQuiz(quizId: String): Result<Unit> =
        runCatching {
            quizCollectionRef.document(quizId).delete().await()
        }
}
