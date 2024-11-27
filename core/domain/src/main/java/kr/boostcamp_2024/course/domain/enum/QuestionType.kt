package kr.boostcamp_2024.course.domain.enum

import kr.boostcamp_2024.course.domain.enum.QuestionType.Blank
import kr.boostcamp_2024.course.domain.enum.QuestionType.Choice

sealed class QuestionType(
    val value: String,
) {
    data object Choice : QuestionType("choice")

    data object Blank : QuestionType("blank")
}

fun String.toQuestionType(): QuestionType = when (this) {
    "choice" -> Choice
    "blank" -> Blank
    else -> throw IllegalArgumentException("Invalid QuestionType value: $this")
}
