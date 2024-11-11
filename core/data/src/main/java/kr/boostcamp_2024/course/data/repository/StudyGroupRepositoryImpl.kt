package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import javax.inject.Inject

class StudyGroupRepositoryImpl @Inject constructor(
	firestore: FirebaseFirestore,
) : StudyGroupRepository {
	private val studyGroupCollectionRef = firestore.collection("StudyGroup")
}
