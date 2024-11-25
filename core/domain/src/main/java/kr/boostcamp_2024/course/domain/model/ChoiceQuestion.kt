package kr.boostcamp_2024.course.domain.model

sealed class Question {
    abstract val id: String
    abstract val title: String
    abstract val solution: String?
    abstract val type: String
}

data class ChoiceQuestion(
    override val id: String,
    override val title: String,
    val description: String,
    override val solution: String?,
    val answer: Int,
    val choices: List<String>,
    val userAnswers: List<Int>,
    override val type: String,
) : Question()
