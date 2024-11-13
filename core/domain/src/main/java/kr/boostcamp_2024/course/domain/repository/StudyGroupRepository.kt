package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.StudyGroup

interface StudyGroupRepository {
    suspend fun getStudyGroup(studyGroupIds: List<String>): Result<List<StudyGroup>>
    suspend fun getStudyGroupName(studyGroupId: String): Result<String>
}
