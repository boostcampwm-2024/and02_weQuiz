package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.UserOmr

interface QuestionRepository {
    suspend fun getQuestions(questionIds: List<String>): Result<List<Question>>

    suspend fun submitQuiz(userOmr: UserOmr): Result<String>

    suspend fun addUserOmrToQuiz(quizId: String, userOmrId: String): Result<Unit>
}
