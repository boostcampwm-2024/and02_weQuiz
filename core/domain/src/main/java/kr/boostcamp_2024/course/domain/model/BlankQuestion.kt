package kr.boostcamp_2024.course.domain.model

data class BlankQuestion(
    override val id: String,
    override val title: String,
    override val solution: String?,
    val questionContent: List<Map<String, String>>,
    override val type: String,
) : Question()
