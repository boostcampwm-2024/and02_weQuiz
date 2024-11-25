package kr.boostcamp_2024.course.data.model

import kr.boostcamp_2024.course.domain.model.BlankQuestionContent
import kr.boostcamp_2024.course.domain.model.TextContent

sealed interface BlankQuestionContentDTO {
    fun toVO(): BlankQuestionContent
}

data class TextContentDTO(
    val type: String? = null,
    val text: String? = null,
) : BlankQuestionContentDTO {
    override fun toVO(): BlankQuestionContent = TextContent(
        type = requireNotNull(type),
        text = requireNotNull(text),
    )
}

fun TextContent.toDTO() = TextContentDTO(
    type = this.type,
    text = this.text,
)
