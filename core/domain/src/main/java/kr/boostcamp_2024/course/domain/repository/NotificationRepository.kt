package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.Notification

interface NotificationRepository {
    suspend fun getNotifications(userId: String): List<Notification>

    suspend fun deleteNotification(notificationId: String)

    suspend fun addNotification(groupId: String, userId: String)

    suspend fun deleteNotificationByStudyGroupId(studyGroupId: String)
}
