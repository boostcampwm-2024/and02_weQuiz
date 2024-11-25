package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.BlankQuestionCreationInfo
import kr.boostcamp_2024.course.domain.model.Question

sealed interface QuestionDTO {
    fun toVO(questionId: String): Question
}

data class BlankQuestionDTO(
    val title: String? = null,
    val solution: String? = null,
    @get:PropertyName("question_content")
    @set:PropertyName("question_content")
    var questionContent: List<Map<String, String>>? = null,
    val type: String? = null,
) : QuestionDTO {
    override fun toVO(questionId: String): Question = BlankQuestion(
        id = questionId,
        title = requireNotNull(title),
        solution = solution,
        questionContent = requireNotNull(questionContent),
        type = requireNotNull(type),
    )
}

fun BlankQuestionCreationInfo.toDTO() = BlankQuestionDTO(
    title = this.title,
    solution = this.solution,
    questionContent = this.questionContent,
    type = this.type,
)
