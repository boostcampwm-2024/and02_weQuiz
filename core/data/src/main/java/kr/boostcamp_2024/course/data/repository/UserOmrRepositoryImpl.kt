package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.UserOmrDTO
import kr.boostcamp_2024.course.domain.model.UserOmr
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import javax.inject.Inject

class UserOmrRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : UserOmrRepository {
    private val userOmrCollectionRef = firestore.collection("UserOmr")

    override suspend fun getUserOmr(userOmrId: String): Result<UserOmr> =
        runCatching {
            val document = userOmrCollectionRef.document(userOmrId).get().await()
            val response = document.toObject(UserOmrDTO::class.java)
            requireNotNull(response).toVO(userOmrId)
        }
}
