package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.toDTO
import kr.boostcamp_2024.course.domain.model.QuestionCreationInfo
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : QuestionRepository {
    private val questionCollectionRef = firestore.collection("Question")

    override suspend fun createQuestion(questionCreationInfo: QuestionCreationInfo): Result<String> = runCatching {
        val document = questionCollectionRef.add(questionCreationInfo.toDTO()).await()
        document.id
    }
}
