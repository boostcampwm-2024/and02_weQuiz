package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.Notification

data class NotificationDTO(
    @get:PropertyName("group_id")
    @set:PropertyName("group_id")
    var groupId: String? = null,
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,
) {
    fun toVO(notificationId: String): Notification = Notification(
        id = notificationId,
        userid = requireNotNull(userId),
        groupId = requireNotNull(groupId),
    )
}

