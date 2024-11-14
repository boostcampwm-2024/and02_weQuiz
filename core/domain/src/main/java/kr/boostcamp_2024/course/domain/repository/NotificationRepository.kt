package kr.boostcamp_2024.course.domain.repository

interface NotificationRepository {
    suspend fun addNotification(groupId: String, userId: String): Result<Unit>
}
