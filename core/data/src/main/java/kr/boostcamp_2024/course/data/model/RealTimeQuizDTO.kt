package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz

interface BaseQuizDTO

data class RealTimeQuizDTO(
    val title: String? = null,
    val description: String? = null,
    val questions: List<String>? = null,
    @get:PropertyName("user_omrs")
    @set:PropertyName("user_omrs")
    var userOmrs: List<String>? = null,
    @get:PropertyName("current_question")
    @set:PropertyName("current_question")
    var currentQuestion: Int? = null,
    @get:PropertyName("owner_id")
    @set:PropertyName("owner_id")
    var ownerId: String? = null,
    val type: String? = null,
    @get:PropertyName("is_started")
    @set:PropertyName("is_started")
    var isStarted: Boolean? = null,
    @get:PropertyName("is_finished")
    @set:PropertyName("is_finished")
    var isFinished: Boolean? = null,
    @get:PropertyName("waiting_users")
    @set:PropertyName("waiting_users")
    var waitingUsers: Int? = null,
    @get:PropertyName("quiz_image_url")
    @set:PropertyName("quiz_image_url")
    var quizImageUrl: String? = null,
) : BaseQuizDTO {
    fun toVO(quizId: String): RealTimeQuiz = RealTimeQuiz(
        id = quizId,
        title = requireNotNull(title),
        description = description,
        questions = requireNotNull(questions),
        userOmrs = requireNotNull(userOmrs),
        currentQuestion = requireNotNull(currentQuestion),
        ownerId = requireNotNull(ownerId),
        type = requireNotNull(type),
        isStarted = requireNotNull(isStarted),
        isFinished = requireNotNull(isFinished),
        waitingUsers = requireNotNull(waitingUsers),
    )
}
