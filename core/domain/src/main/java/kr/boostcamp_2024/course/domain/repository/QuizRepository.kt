package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.QuizCreationInfo

interface QuizRepository {
    suspend fun createQuiz(quizCreateInfo: QuizCreationInfo): Result<String>
}