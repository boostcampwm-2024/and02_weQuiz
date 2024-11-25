package kr.boostcamp_2024.course.domain.repository

interface AuthRepository {
    suspend fun storeUserKey(userKey: String): Result<Unit>

    suspend fun getUserKey(): Result<String>

    suspend fun removeUserKey(): Result<Unit>
}
