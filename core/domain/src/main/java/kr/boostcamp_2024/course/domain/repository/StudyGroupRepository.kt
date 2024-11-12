package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.StudyGroupCreationInfo

interface StudyGroupRepository {
    suspend fun addStudyGroup(studyGroupCreationInfo: StudyGroupCreationInfo): Result<String>

    suspend fun addStudyGroupToUser(userId: String, studyId: String): Result<String>
}
