package kr.boostcamp_2024.course.domain.model

data class BlankQuestion(
    val id: String,
    val title: String,
    val solution: String?,
    val questionContent: List<BlankQuestionContent>,
    val type: String,
)
