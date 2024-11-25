package kr.boostcamp_2024.course.domain.model

sealed interface Question

class ChoiceQuestion(
    val id: String,
    val title: String,
    val description: String,
    val solution: String?,
    val answer: Int,
    val choices: List<String>,
    val userAnswers: List<Int>,
    val type: String,
) : Question
