package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.QuizDTO
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : QuizRepository {
    private val quizCollectionRef = firestore.collection("Quiz")

    override suspend fun getQuiz(quizId: String): Result<Quiz> {
        return runCatching {
            val document = quizCollectionRef.document(quizId).get().await()
            val response = document.toObject(QuizDTO::class.java)
            requireNotNull(response).toVO()
        }
    }

    override suspend fun updateQuizQuestionList(quizId: String, questionList: List<String>): Result<Unit> {
        return runCatching {
            val document = quizCollectionRef.document(quizId)
            document.update("questions", questionList)
        }
    }
}
