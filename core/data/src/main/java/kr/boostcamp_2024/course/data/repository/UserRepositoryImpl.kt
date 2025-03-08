package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.UserDTO
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.model.UserSubmitInfo
import kr.boostcamp_2024.course.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : UserRepository {
    private val userCollectionRef = firestore.collection("User")

    override suspend fun addUser(userId: String, userSubmitInfo: UserSubmitInfo) {
        userCollectionRef.document(userId).set(
            UserDTO(
                email = userSubmitInfo.email,
                name = userSubmitInfo.name,
                profileUrl = userSubmitInfo.profileImageUrl,
                studyGroups = userSubmitInfo.studyGroups,
            ),
        ).await()
    }

    override suspend fun getUser(userId: String): User {
        val document = userCollectionRef.document(userId).get().await()
        val response = document.toObject(UserDTO::class.java)
        return requireNotNull(response).toVO(userId)
    }

    override suspend fun addStudyGroupToUser(userId: String, studyId: String) {
        val userDocRef = userCollectionRef.document(userId)
        userDocRef.update("study_groups", FieldValue.arrayUnion(studyId)).await()

    }

    override suspend fun getUsers(userIds: List<String>): List<User> =
        userIds.map { userId ->
            val document = userCollectionRef.document(userId).get().await()
            val response = document.toObject(UserDTO::class.java)
            requireNotNull(response).toVO(userId)
        }

    override suspend fun deleteStudyGroupUser(userId: String, studyGroupId: String) {
        val document = userCollectionRef.document(userId)
        document.update("study_groups", FieldValue.arrayRemove(studyGroupId)).await()
    }

    override suspend fun deleteStudyGroupUsers(userIds: List<String>, studyGroupId: String) {
        userIds.forEach { userId ->
            val document = userCollectionRef.document(userId)
            document.update("study_groups", FieldValue.arrayRemove(studyGroupId)).await()
        }
    }

    override suspend fun findUserByEmail(email: String): User {
        val querySnapshot = userCollectionRef.whereEqualTo("email", email).get().await()
        val response = querySnapshot.documents.firstOrNull()?.toObject(UserDTO::class.java)
        return requireNotNull(response).toVO(querySnapshot.documents.first().id)
    }

    override suspend fun updateUser(userId: String, userSubmitInfo: UserSubmitInfo) {
        val userDocRef = userCollectionRef.document(userId)
        val userMap = mapOf(
            "email" to userSubmitInfo.email,
            "name" to userSubmitInfo.name,
            "profile_url" to userSubmitInfo.profileImageUrl,
        )
        userDocRef.update(userMap).await()
    }
}
