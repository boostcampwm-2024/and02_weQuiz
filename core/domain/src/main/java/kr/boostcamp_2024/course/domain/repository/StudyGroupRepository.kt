package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.StudyGroupCreationInfo
import kr.boostcamp_2024.course.domain.model.StudyGroupUpdatedInfo

interface StudyGroupRepository {
    suspend fun addStudyGroup(studyGroupCreationInfo: StudyGroupCreationInfo): String

    suspend fun getStudyGroups(studyGroupIds: List<String>): List<StudyGroup>

    suspend fun getStudyGroup(studyGroupId: String): StudyGroup

    suspend fun getStudyGroupName(studyGroupId: String): String

    suspend fun addCategoryToStudyGroup(studyGroupId: String, categoryId: String)

    suspend fun deleteUser(studyGroupId: String, userId: String)

    suspend fun deleteCategory(studyGroupId: String, categoryId: String)

    suspend fun deleteStudyGroup(studyGroupId: String)

    suspend fun updateStudyGroup(studyGroupId: String, updateInfo: StudyGroupUpdatedInfo)

    suspend fun addUser(studyGroupId: String, userId: String)
}
