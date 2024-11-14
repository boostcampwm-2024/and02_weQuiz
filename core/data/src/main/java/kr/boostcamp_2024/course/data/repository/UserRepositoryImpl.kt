package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.UserDTO
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : UserRepository {
    private val userCollectionRef = firestore.collection("User")

    override suspend fun getUser(userId: String): Result<User> {
        return runCatching {
            val document = userCollectionRef.document(userId).get().await()
            val response = document.toObject(UserDTO::class.java)
            requireNotNull(response).toVO()
        }
    }

    override suspend fun addStudyGroupToUser(userId: String, studyId: String): Result<String> =
        runCatching {
            val userDocRef = userCollectionRef.document(userId)
            userDocRef.update("study_groups", FieldValue.arrayUnion(studyId)).await()
            studyId // TODO 성공 반환값 고민하기
        }

}
