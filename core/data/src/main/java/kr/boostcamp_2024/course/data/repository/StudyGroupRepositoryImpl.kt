package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.StudyGroupDTO
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import javax.inject.Inject

class StudyGroupRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : StudyGroupRepository {
    private val studyGroupCollectionRef = firestore.collection("StudyGroup")

    override suspend fun getStudyGroup(studyGroupId: String): Result<StudyGroup> = runCatching {
        val document = studyGroupCollectionRef.document(studyGroupId).get().await()
        val response = document.toObject(StudyGroupDTO::class.java)
        requireNotNull(response).toVO(studyGroupId)
    }

    override suspend fun getStudyGroups(studyGroupIds: List<String>): Result<List<StudyGroup>> = runCatching {
        studyGroupIds.map { studyGroupId ->
            val document = studyGroupCollectionRef.document(studyGroupId).get().await()
            val response = document.toObject(StudyGroupDTO::class.java)
            requireNotNull(response).toVO(studyGroupId)
        }
    }

    override suspend fun deleteUser(studyGroupId: String, userId: String): Result<Unit> = runCatching {
        val document = studyGroupCollectionRef.document(studyGroupId)
        document.update("users", FieldValue.arrayRemove(userId)).await()
    }
}
