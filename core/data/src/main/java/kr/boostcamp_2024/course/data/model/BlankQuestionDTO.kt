package kr.boostcamp_2024.course.data.model

import com.google.firebase.firestore.PropertyName
import kr.boostcamp_2024.course.domain.model.BlankContent
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.BlankQuestionCreationInfo
import kr.boostcamp_2024.course.domain.model.TextContent

sealed interface QuestionDTO

data class BlankQuestionDTO(
    val title: String? = null,
    val solution: String? = null,
    @get:PropertyName("question_content")
    @set:PropertyName("question_content")
    var questionContent: List<BlankQuestionContentDTO>? = null,
    val type: String? = null,
) : QuestionDTO {
    fun toVO(questionId: String): BlankQuestion = BlankQuestion(
        id = questionId,
        title = requireNotNull(title),
        solution = solution,
        questionContent = requireNotNull(questionContent).map { it.toVO() },
        type = requireNotNull(type),
    )
}

fun BlankQuestionCreationInfo.toDTO() = BlankQuestionDTO(
    title = this.title,
    solution = this.solution,
    questionContent = this.questionContent.map {
        when (it) {
            is BlankContent -> it.toDTO()
            is TextContent -> it.toDTO()
        }
    },
    type = this.type,
)
