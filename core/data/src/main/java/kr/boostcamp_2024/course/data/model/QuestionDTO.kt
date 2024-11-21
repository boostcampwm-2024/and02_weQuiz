package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.QuestionCreationInfo

data class QuestionDTO(
    val title: String? = null,
    val description: String? = null,
    val solution: String? = null,
    val answer: Int? = null,
    val choices: List<String>? = null,
    @get:PropertyName("current_submit")
    @set:PropertyName("current_submit")
    var currentSubmit: Int? = null,
) {
    fun toVO(questionId: String): Question = Question(
        id = questionId,
        title = requireNotNull(title),
        description = requireNotNull(description),
        solution = solution,
        answer = requireNotNull(answer),
        choices = requireNotNull(choices),
        currentSubmit = requireNotNull(currentSubmit),
    )
}

fun QuestionCreationInfo.toDTO() = QuestionDTO(
    title = this.title,
    description = this.description,
    solution = this.solution,
    answer = this.answer,
    choices = this.choices,
    currentSubmit = 0,
)
