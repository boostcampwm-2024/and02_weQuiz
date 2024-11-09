package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.UserResponseVO

interface UserRepository {
    suspend fun getUser(userId: String): Result<UserResponseVO>
}