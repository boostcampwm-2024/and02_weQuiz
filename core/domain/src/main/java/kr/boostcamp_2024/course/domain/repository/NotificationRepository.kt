package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(userId: String): Result<List<Notification>>

    suspend fun deleteNotification(notificationId: String): Result<Unit>

    suspend fun addNotification(groupId: String, userId: String): Result<Unit>

    suspend fun deleteNotificationByStudyGroupId(studyGroupId: String): Result<Unit>
}
