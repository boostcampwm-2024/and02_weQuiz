package kr.boostcamp_2024.course.domain.model

data class BlankQuestion(
    override val id: String,
    override val title: String,
    override val solution: String?,
    override val userAnswers: List<String>,
    val questionContent: List<Map<String, String>>,
) : Question()
