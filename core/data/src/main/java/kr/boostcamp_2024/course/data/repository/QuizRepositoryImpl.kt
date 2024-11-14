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

    override suspend fun createQuiz(quizCreateInfo: QuizCreationInfo): Result<String> =
        runCatching {
            val newQuiz = QuizDTO(
                title = quizCreateInfo.quizTitle,
                description = quizCreateInfo.quizDescription,
                startTime = quizCreateInfo.quizDate,
                solveTime = quizCreateInfo.quizSolveTime,
                questions = emptyList(),
                userOmrs = emptyList(),
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

    override suspend fun addUserOmrToQuiz(quizId: String, userOmrId: String): Result<Unit> =
        runCatching {
            quizCollectionRef.document(quizId)
                .update("user_omrs", FieldValue.arrayUnion(userOmrId))
                .await()
        }
}
