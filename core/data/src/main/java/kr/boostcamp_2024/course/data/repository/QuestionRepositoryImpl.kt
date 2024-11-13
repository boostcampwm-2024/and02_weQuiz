package kr.boostcamp_2024.course.data.repository

import android.provider.SyncStateContract.Helpers.update
import android.util.Log
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.QuestionDTO
import kr.boostcamp_2024.course.data.model.UserOmrDTO
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.UserOmr
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : QuestionRepository {
    private val questionCollectionRef = firestore.collection("Question")
    private val userOmrCollectionRef = firestore.collection("UserOmr")
    private val quizCollectionRef = firestore.collection("Quiz")

    override suspend fun getQuestions(questionIds: List<String>): Result<List<Question>> {
        return runCatching {
            Log.d("QuestionRepositoryImpl", "getQuestions: $questionIds")
            val snapshot = questionCollectionRef.whereIn(FieldPath.documentId(), questionIds).get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(QuestionDTO::class.java)?.toVO()
            }
        }
    }

    override suspend fun submitQuiz(userOmr: UserOmr): Result<String> =
        runCatching {
            val userOmrDTO = UserOmrDTO(
                userId = userOmr.userId,
                quizId = userOmr.quizId,
                answers = userOmr.answers,
            )
            userOmrCollectionRef.add(userOmrDTO).await().id
        }
    override suspend fun addUserOmrToQuiz(quizId: String, userOmrId: String): Result<Unit> =
        runCatching {
           quizCollectionRef.document(quizId)
                .update("user_omrs", FieldValue.arrayUnion(userOmrId))
                .await()
        }
}
