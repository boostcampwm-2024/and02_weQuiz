package kr.boostcamp_2024.course.domain.model

data class BlankQuestion(
    val id: String,
    override val title: String,
    override val solution: String?,
    val questionContent: List<BlankQuestionContent>,
    override val type: String,
) : Question()
