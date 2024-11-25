package kr.boostcamp_2024.course.domain.model

data class BlankQuestionCreationInfo(
    val title: String,
    val solution: String?,
    val questionContent: List<Map<String, String>>,
    val type: String = "blank",
)
