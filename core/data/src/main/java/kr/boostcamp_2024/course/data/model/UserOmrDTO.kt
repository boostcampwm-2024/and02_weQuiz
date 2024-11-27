package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.UserOmr

data class UserOmrDTO(
    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var userId: String? = null,
    @get:PropertyName("quiz_id")
    @set:PropertyName("quiz_id")
    var quizId: String? = null,
    val answers: List<Any>? = null,
) {
    fun toVO(id: String): UserOmr = UserOmr(
        id = id,
        userId = requireNotNull(userId),
        quizId = requireNotNull(quizId),
        answers = requireNotNull(answers),
    )
}
