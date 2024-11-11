package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.QuizCreateInfo

interface QuizRepository {
    suspend fun createQuiz(quizCreateInfo: QuizCreateInfo): Result<String>
}