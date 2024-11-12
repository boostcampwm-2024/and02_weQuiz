package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.StudyGroupDTO
import kr.boostcamp_2024.course.domain.model.StudyGroupCreationInfo
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import javax.inject.Inject

class StudyGroupRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : StudyGroupRepository {
    private val studyGroupCollectionRef = firestore.collection("StudyGroup")
    private val userCollectionRef = firestore.collection("User")

    override suspend fun addStudyGroup(studyGroupCreationInfo: StudyGroupCreationInfo): Result<String> {
        return runCatching {
            val request = StudyGroupDTO(
                name = studyGroupCreationInfo.name,
                description = studyGroupCreationInfo.description,
                maxUserNum = studyGroupCreationInfo.maxUserNum,
                ownerId = studyGroupCreationInfo.ownerId,
                users = listOf(studyGroupCreationInfo.ownerId),
                categories = emptyList()
            )
            val document = studyGroupCollectionRef.document()
            document.set(request).await()

            val result = document.id
            result
        }
    }

    override suspend fun addStudyGroupToUser(userId: String, studyId: String): Result<String> {
        return runCatching {
            val userDocRef = userCollectionRef.document(userId)
            userDocRef.update("study_groups", FieldValue.arrayUnion(studyId)).await()
            studyId // TODO 성공 반환값 고민하기
        }
    }
    override suspend fun getStudyGroup(studyGroupIds: List<String>): Result<List<StudyGroup>> =
        runCatching {
            studyGroupIds.map { studyGroupId ->
                val document = studyGroupCollectionRef.document(studyGroupId).get().await()
                val response = document.toObject(StudyGroupDTO::class.java)
                requireNotNull(response).toVO(studyGroupId)
            }
        }
}
