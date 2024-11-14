package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.NotificationDTO
import kr.boostcamp_2024.course.domain.model.Notification
import kr.boostcamp_2024.course.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : NotificationRepository {
    private val notificationCollectionRef = firestore.collection("Notification")

    override suspend fun getNotifications(userId: String): Result<List<Notification>> =
        runCatching {
            val notifications = mutableListOf<Notification>()
            val snapshot = notificationCollectionRef.whereEqualTo("user_id", userId).get().await()
            for (document in snapshot.documents) {
                val notification = document.toObject(NotificationDTO::class.java)
                notifications.add(requireNotNull(notification).toVO(document.id))
            }
            notifications
        }

    override suspend fun deleteNotification(notificationId: String): Result<Unit> =
        runCatching {
            notificationCollectionRef.document(notificationId).delete().await()
        }

    override suspend fun addNotification(groupId: String, userId: String): Result<Unit> =
        runCatching {
            val notification = mapOf(
                "group_id" to groupId,
                "user_id" to userId,
            )
            notificationCollectionRef.add(notification).await()
        }
}
