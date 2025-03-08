package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.model.UserSubmitInfo

interface UserRepository {
    suspend fun addUser(userId: String, userSubmitInfo: UserSubmitInfo)

    suspend fun getUsers(userIds: List<String>): List<User>

    suspend fun getUser(userId: String): User

    suspend fun findUserByEmail(email: String): User

    suspend fun addStudyGroupToUser(userId: String, studyId: String)

    suspend fun deleteStudyGroupUser(userId: String, studyGroupId: String)

    suspend fun deleteStudyGroupUsers(userIds: List<String>, studyGroupId: String)

    suspend fun updateUser(userId: String, userSubmitInfo: UserSubmitInfo)
}
