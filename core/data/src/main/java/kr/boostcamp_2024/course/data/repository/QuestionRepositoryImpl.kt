package kr.boostcamp_2024.course.data.repository

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.data.model.QuestionDTO
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : QuestionRepository {
    private val questionCollectionRef = firestore.collection("Question")

    override suspend fun getQuestions(questionIds: List<String>): Result<List<Question>> {
        return runCatching {
            Log.d("QuestionRepositoryImpl", "getQuestions: $questionIds")
            val snapshot = questionCollectionRef.whereIn(FieldPath.documentId(), questionIds).get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(QuestionDTO::class.java)?.toVO()
            }
        }
    }

//    override suspend fun submitQuiz(quizSubmission: QuizSubmission): Result<Boolean> {
//        return runCatching {
//            val quizDocument = quizCollectionRef.document(quizSubmission.quizId)
//            val userAnswers = mapOf(
//                "userId" to quizSubmission.userId,
//                "answers" to quizSubmission.answers
//            )
//            quizDocument.collection("user_omr").add(userAnswers).await()
//            true
//        }
//    }
}
