package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.Quiz

interface QuizRepository {
    suspend fun getQuiz(quizId: String): Result<Quiz>
    suspend fun updateQuizQuestionList(quizId: String, questionList: List<String>): Result<Unit>
}
