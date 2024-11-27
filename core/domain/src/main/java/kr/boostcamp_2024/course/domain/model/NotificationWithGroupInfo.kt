package kr.boostcamp_2024.course.domain.model

data class NotificationWithGroupInfo(
    val notification: Notification,
    val studyGroupName: String,
    val studyGroupImgUrl: String?,
)
