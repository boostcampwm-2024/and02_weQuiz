package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldPath
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

    override suspend fun getQuestions(questionIds: List<String>): Result<List<Question>> =
        runCatching {
            val snapshot = questionCollectionRef.whereIn(FieldPath.documentId(), questionIds).get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(QuestionDTO::class.java)?.toVO()
            }
        }
}
