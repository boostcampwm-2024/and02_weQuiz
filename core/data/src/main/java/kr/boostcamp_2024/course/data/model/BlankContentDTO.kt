package kr.boostcamp_2024.course.data.model

import kr.boostcamp_2024.course.domain.model.BlankContent
import kr.boostcamp_2024.course.domain.model.BlankQuestionContent

data class BlankContentDTO(
    val type: String? = null,
    val text: String? = null,
) : BlankQuestionContentDTO {
    override fun toVO(): BlankQuestionContent = BlankContent(
        type = requireNotNull(type),
        text = requireNotNull(text),
    )
}

fun BlankContent.toDTO() = BlankContentDTO(
    type = this.type,
    text = this.text,
)
