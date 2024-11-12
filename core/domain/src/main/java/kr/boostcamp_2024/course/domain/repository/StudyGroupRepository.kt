package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.StudyGroupCreationInfo
import kr.boostcamp_2024.course.domain.model.StudyGroup

interface StudyGroupRepository {
    suspend fun addStudyGroup(studyGroupCreationInfo: StudyGroupCreationInfo): Result<String>

    suspend fun addStudyGroupToUser(userId: String, studyId: String): Result<String>

    suspend fun getStudyGroup(studyGroupIds: List<String>): Result<List<StudyGroup>>
}
