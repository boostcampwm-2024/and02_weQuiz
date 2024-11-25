package kr.boostcamp_2024.course.domain.model

sealed interface BlankQuestionContent

data class TextContent(
    val type: String,
    val text: String,
) : BlankQuestionContent
