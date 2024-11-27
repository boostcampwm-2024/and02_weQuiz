package kr.boostcamp_2024.course.domain.model

sealed class Question {
    abstract val id: String
    abstract val title: String
    abstract val solution: String?
    abstract val userAnswers: List<Any>
}

data class ChoiceQuestion(
    override val id: String,
    override val title: String,
    val description: String,
    override val solution: String?,
    val answer: Int,
    val choices: List<String>,
    override val userAnswers: List<Int>,
) : Question()
