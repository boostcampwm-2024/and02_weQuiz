package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.Quiz

data class QuizDTO(
    val title: String? = null,
    val description: String? = null,

    @get:PropertyName("start_time")
    @set:PropertyName("start_time")
    var startTime: String? = null,

    @get:PropertyName("solve_time")
    @set:PropertyName("solve_time")
    var solveTime: Int? = null,
    val questions: List<String>? = null,

    @get:PropertyName("user_omrs")
    @set:PropertyName("user_omrs")
    var userOmrs: List<String>? = null
) {
    fun toVO(quizId: String): Quiz = Quiz(
        id = quizId,
        title = requireNotNull(title),
        description = requireNotNull(description),
        startTime = requireNotNull(startTime),
        solveTime = requireNotNull(solveTime),
        questions = requireNotNull(questions),
        userOmrs = requireNotNull(userOmrs)
    )
}