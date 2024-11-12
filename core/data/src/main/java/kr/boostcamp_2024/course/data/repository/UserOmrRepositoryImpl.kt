package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import javax.inject.Inject

class UserOmrRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : UserOmrRepository {
    private val userOmrCollectionRef = firestore.collection("UserOmr")
}
