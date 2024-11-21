package kr.boostcamp_2024.course.domain.repository

import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.QuizCreationInfo

interface QuizRepository {
    suspend fun getQuiz(quizId: String): Result<BaseQuiz>

    suspend fun getQuizList(quizIdList: List<String>): Result<List<BaseQuiz>>

    suspend fun addQuestionToQuiz(quizId: String, questionId: String): Result<Unit>

    suspend fun createQuiz(quizCreateInfo: QuizCreationInfo): Result<String>

    suspend fun addUserOmrToQuiz(quizId: String, userOmrId: String): Result<Unit>

    suspend fun editQuiz(quizId: String, quizCreateInfo: QuizCreationInfo): Result<Unit>

    suspend fun deleteQuiz(quizId: String): Result<Unit>

    suspend fun deleteQuizzes(quizzes: List<String>): Result<Unit>
}
