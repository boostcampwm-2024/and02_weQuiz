package kr.boostcamp_2024.course.data.model

import kr.boostcamp_2024.course.domain.model.Question

data class QuestionDTO(
    val title: String? = null,
    val description: String? = null,
    val choices: List<String>? = null,
    val answer: Int? = null,
    val solution: String? = null,
) {
    fun toVO(): Question {
        return Question(
            title = requireNotNull(title),
            description = requireNotNull(description),
            choices = requireNotNull(choices),
            solution = solution ?: "",
            answer = answer ?: -1,
        )
    }
}
