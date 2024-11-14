package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.QuestionCreationInfo

interface QuestionRepository {
    suspend fun createQuestion(
        questionCreationInfo: QuestionCreationInfo,
    ): Result<String>
}
