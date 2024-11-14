package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.User

interface UserRepository {
    suspend fun getUsers(userIds: List<String>): Result<List<User>>

    suspend fun getUser(userId: String): Result<User>

    suspend fun addStudyGroupToUser(userId: String, studyId: String): Result<Unit>
}
