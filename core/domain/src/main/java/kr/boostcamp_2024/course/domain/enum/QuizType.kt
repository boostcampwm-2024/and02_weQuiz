package kr.boostcamp_2024.course.domain.enum

sealed class QuizType(
    val value: String,
) {
    data object RealTime : QuizType("realTime")

    data object General : QuizType("general")

    companion object {
        fun getQuizTypeFromValue(value: String): QuizType = when (value) {
            "realTime" -> RealTime
            "general" -> General
            else -> throw IllegalArgumentException("Invalid QuizType value: $value")
        }
    }
}
