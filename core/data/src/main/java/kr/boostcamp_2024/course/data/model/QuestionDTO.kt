package kr.boostcamp_2024.course.data.model

import kr.boostcamp_2024.course.domain.model.Question

data class QuestionDTO(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val solution: String? = null,
    val answer: Int? = null,
    val choices: List<String>? = null,
) {
    fun toVO(questionId: String): Question = Question(
        id = questionId,
        title = requireNotNull(title),
        description = requireNotNull(description),
        solution = solution,
        answer = requireNotNull(answer),
        choices = requireNotNull(choices),
    )
}
