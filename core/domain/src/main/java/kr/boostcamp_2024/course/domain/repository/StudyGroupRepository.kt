package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.StudyGroupCreationInfo

interface StudyGroupRepository {
    suspend fun addStudyGroup(studyGroupCreationInfo: StudyGroupCreationInfo): Result<String>

    suspend fun getStudyGroups(studyGroupIds: List<String>): Result<List<StudyGroup>>

    suspend fun getStudyGroup(studyGroupId: String): Result<StudyGroup>

    suspend fun getStudyGroupName(studyGroupId: String): Result<String>

    suspend fun addCategoryToStudyGroup(studyGroupId: String, categoryId: String): Result<Unit>

    suspend fun deleteUser(studyGroupId: String, userId: String): Result<Unit>

    suspend fun deleteStudyGroup(studyGroupId: String): Result<Unit>
}
