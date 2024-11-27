package kr.boostcamp_2024.course.domain.enum

sealed class BlankContentType(
    val value: String,
) {
    data object Text : BlankContentType("text")

    data object Blank : BlankContentType("blank")
}

fun String.toBlankContentType(): BlankContentType = when (this) {
    "text" -> BlankContentType.Text
    "blank" -> BlankContentType.Blank
    else -> throw IllegalArgumentException("Invalid BlankContentType value: $this")
}
