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

    override suspend fun addStudyGroup(studyGroupCreationInfo: StudyGroupCreationInfo): String {
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
        return result
    }

    override suspend fun getStudyGroup(studyGroupId: String): StudyGroup {
        val document = studyGroupCollectionRef.document(studyGroupId).get().await()
        val response = document.toObject(StudyGroupDTO::class.java)
        return requireNotNull(response).toVO(studyGroupId)
    }

    override suspend fun getStudyGroups(studyGroupIds: List<String>): List<StudyGroup> =
        studyGroupIds.map { studyGroupId ->
            val document = studyGroupCollectionRef.document(studyGroupId).get().await()
            val response = document.toObject(StudyGroupDTO::class.java)
            requireNotNull(response).toVO(studyGroupId)
        }

    override suspend fun deleteUser(studyGroupId: String, userId: String) {
        val document = studyGroupCollectionRef.document(studyGroupId)
        document.update("users", FieldValue.arrayRemove(userId)).await()
    }

    override suspend fun deleteStudyGroup(studyGroupId: String) {
        studyGroupCollectionRef.document(studyGroupId).delete().await()
    }

    override suspend fun deleteCategory(studyGroupId: String, categoryId: String) {
        val document = studyGroupCollectionRef.document(studyGroupId)
        document.update("categories", FieldValue.arrayRemove(categoryId)).await()
    }

    override suspend fun addCategoryToStudyGroup(studyGroupId: String, categoryId: String) {
        val document = studyGroupCollectionRef.document(studyGroupId)
        document.update("categories", FieldValue.arrayUnion(categoryId)).await()
    }

    override suspend fun getStudyGroupName(studyGroupId: String): String {
        val document = studyGroupCollectionRef.document(studyGroupId).get().await()
        val response = document.toObject(StudyGroupDTO::class.java)
        val studyGroupName = requireNotNull(response?.name)
        return studyGroupName
    }

    override suspend fun updateStudyGroup(studyGroupId: String, updateInfo: StudyGroupUpdatedInfo) {
        val updatedInfoMap = hashMapOf<String, Any?>(
            "study_group_image_url" to updateInfo.studyGroupImageUrl,
            "name" to updateInfo.name,
            "description" to updateInfo.description,
            "max_user_num" to updateInfo.maxUserNum,
        )
        studyGroupCollectionRef.document(studyGroupId).update(updatedInfoMap).await()
    }

    override suspend fun addUser(studyGroupId: String, userId: String) {
        val document = studyGroupCollectionRef.document(studyGroupId)
        document.update("users", FieldValue.arrayUnion(userId)).await()
    }
}
