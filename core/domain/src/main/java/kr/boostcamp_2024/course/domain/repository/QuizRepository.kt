package kr.boostcamp_2024.course.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.QuizCreationInfo
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz

interface QuizRepository {
    suspend fun getQuiz(quizId: String): BaseQuiz

    suspend fun getQuizList(quizIdList: List<String>): List<BaseQuiz>

    suspend fun addQuestionToQuiz(quizId: String, questionId: String)

    suspend fun createQuiz(quizCreateInfo: QuizCreationInfo, ownerId: String?): String

    suspend fun addUserOmrToQuiz(quizId: String, userOmrId: String)

    suspend fun editQuiz(quizId: String, quizCreateInfo: QuizCreationInfo, selectedQuizTypeIndex: Int)

    suspend fun deleteQuiz(quizId: String)

    suspend fun deleteQuizzes(quizzes: List<String>)

    fun observeRealTimeQuiz(quizId: String): Flow<RealTimeQuiz>

    suspend fun setQuizFinished(quizId: String)

    suspend fun updateQuizCurrentQuestion(quizId: String, currentQuestion: Int)

    suspend fun startRealTimeQuiz(quizId: String)

    suspend fun waitingRealTimeQuiz(quizId: String, waiting: Boolean, userId: String)

    fun observeQuiz(quizId: String): Flow<BaseQuiz>
}
