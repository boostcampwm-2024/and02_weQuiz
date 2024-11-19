package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.StudyGroupDTO
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.StudyGroupCreationInfo
import kr.boostcamp_2024.course.domain.model.StudyGroupUpdatedInfo
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import javax.inject.Inject

class StudyGroupRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : StudyGroupRepository {
    private val studyGroupCollectionRef = firestore.collection("StudyGroup")

    override suspend fun addStudyGroup(studyGroupCreationInfo: StudyGroupCreationInfo): Result<String> =
        runCatching {
            val request = StudyGroupDTO(
                studyGroupImageUrl = studyGroupCreationInfo.studyGroupImageUrl,
                name = studyGroupCreationInfo.name,
                description = studyGroupCreationInfo.description,
                maxUserNum = studyGroupCreationInfo.maxUserNum,
                ownerId = studyGroupCreationInfo.ownerId,
                users = listOf(studyGroupCreationInfo.ownerId),
                categories = emptyList(),
            )
            val document = studyGroupCollectionRef.document()
            document.set(request).await()

            val result = document.id
            result
        }

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

    override suspend fun deleteStudyGroup(studyGroupId: String): Result<Unit> = runCatching {
        studyGroupCollectionRef.document(studyGroupId).delete().await()
    }

    override suspend fun addCategoryToStudyGroup(studyGroupId: String, categoryId: String): Result<Unit> =
        runCatching {
            val document = studyGroupCollectionRef.document(studyGroupId)
            document.update("categories", FieldValue.arrayUnion(categoryId)).await()
        }

    override suspend fun getStudyGroupName(studyGroupId: String): Result<String> =
        runCatching {
            val document = studyGroupCollectionRef.document(studyGroupId).get().await()
            val response = document.toObject(StudyGroupDTO::class.java)
            val studyGroupName = requireNotNull(response?.name)
            studyGroupName
        }

    override suspend fun updateStudyGroup(studyGroupId: String, updatedInfo: StudyGroupUpdatedInfo): Result<Unit> =
        runCatching {
            val updatedInfoMap = hashMapOf<String, Any?>(
                "name" to updatedInfo.name,
                "description" to updatedInfo.description,
                "max_user_num" to updatedInfo.maxUserNum,
            )
            studyGroupCollectionRef.document(studyGroupId).update(updatedInfoMap).await()
        }
}
