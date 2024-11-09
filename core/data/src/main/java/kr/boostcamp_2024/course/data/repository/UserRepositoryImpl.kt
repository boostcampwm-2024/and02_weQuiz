package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.UserResponseDTO
import kr.boostcamp_2024.course.domain.model.UserResponseVO
import kr.boostcamp_2024.course.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : UserRepository {
    private val userCollectionRef = firestore.collection("User")

    override suspend fun getUser(userId: String): Result<UserResponseVO> {
        return try {
            val document = userCollectionRef.document(userId).get().await()
            val response = document.toObject(UserResponseDTO::class.java)
            if (response != null) {
                Result.success(response.toVO())
            } else {
                Result.failure(Exception("User not found"))
            }
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}