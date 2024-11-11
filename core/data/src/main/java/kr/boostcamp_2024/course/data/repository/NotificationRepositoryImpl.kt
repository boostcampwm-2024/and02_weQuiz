package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kr.boostcamp_2024.course.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : NotificationRepository {
    private val notificationCollectionRef = firestore.collection("Notification")
}
