package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.Question

interface QuestionRepository {
    suspend fun getQuestion(questionId: String): Result<Question>
}
