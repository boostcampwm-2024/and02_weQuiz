package kr.boostcamp_2024.course.domain.repository

interface AuthRepository {
    suspend fun login(idToken: String)

    suspend fun loginExperience()

    fun getUserKey(): String

    fun logout()

}
