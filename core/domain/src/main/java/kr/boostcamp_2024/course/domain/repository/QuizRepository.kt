package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.model.QuizCreationInfo

interface QuizRepository {
    suspend fun createQuiz(quizCreateInfo: QuizCreationInfo): Result<String>

    suspend fun getQuiz(quizId: String): Result<Quiz>

    suspend fun getQuizList(quizIdList: List<String>): Result<List<Quiz>>
}
