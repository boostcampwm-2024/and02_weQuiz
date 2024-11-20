package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.model.UserCreationInfo

interface UserRepository {
    suspend fun addUser(userId: String, userCreationInfo: UserCreationInfo): Result<Unit>

    suspend fun getUsers(userIds: List<String>): Result<List<User>>

    suspend fun getUser(userId: String): Result<User>

    suspend fun findUserByEmail(email: String): Result<User>

    suspend fun addStudyGroupToUser(userId: String, studyId: String): Result<Unit>

    suspend fun deleteStudyGroupUser(userId: String, studyGroupId: String): Result<Unit>

    suspend fun deleteStudyGroupUsers(userIds: List<String>, studyGroupId: String): Result<Unit>
}
