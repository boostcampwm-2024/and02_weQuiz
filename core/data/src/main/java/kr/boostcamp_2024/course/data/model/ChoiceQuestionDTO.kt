package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestionCreationInfo

data class ChoiceQuestionDTO(
    val title: String? = null,
    val description: String? = null,
    val solution: String? = null,
    val answer: Int? = null,
    val choices: List<String>? = null,
    @get:PropertyName("user_answers")
    @set:PropertyName("user_answers")
    var userAnswers: List<Int>? = null,
    var type: String? = null,
) {
    fun toVO(questionId: String): ChoiceQuestion = ChoiceQuestion(
        id = questionId,
        title = requireNotNull(title),
        description = requireNotNull(description),
        solution = solution,
        answer = requireNotNull(answer),
        choices = requireNotNull(choices),
        userAnswers = requireNotNull(userAnswers),
        type = requireNotNull(type),
    )
}

fun ChoiceQuestionCreationInfo.toDTO() = ChoiceQuestionDTO(
    title = this.title,
    description = this.description,
    solution = this.solution,
    answer = this.answer,
    choices = this.choices,
    userAnswers = List(4) { 0 },
    type = this.type,
)
