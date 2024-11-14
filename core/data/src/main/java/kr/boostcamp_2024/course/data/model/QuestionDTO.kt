package kr.boostcamp_2024.course.data.model

import kr.boostcamp_2024.course.domain.model.QuestionCreationInfo

data class QuestionDTO(
    val title: String,
    val description: String,
    val solution: String?,
    val answer: Int,
    val choices: List<String>,
)

fun QuestionCreationInfo.toDTO() = QuestionDTO(
    title = this.title,
    description = this.description,
    solution = this.solution,
    answer = this.answer,
    choices = this.choices,
)
