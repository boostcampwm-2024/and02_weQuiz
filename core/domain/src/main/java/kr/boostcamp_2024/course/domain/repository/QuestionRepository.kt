package kr.boostcamp_2024.course.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.QuestionCreationInfo

interface QuestionRepository {
    suspend fun getQuestions(questionIds: List<String>): Result<List<Question>>

    suspend fun getQuestion(questionId: String): Result<Question>

    fun observeQuestion(questionId: String): Flow<Question>

    suspend fun getRealTimeQuestions(questionIds: List<String>): Result<List<Flow<Question>>>

    suspend fun createQuestion(
        questionCreationInfo: QuestionCreationInfo,
    ): Result<String>

    suspend fun deleteQuestions(questionIds: List<String>): Result<Unit>

    suspend fun updateCurrentSubmit(questionId: String, selectedIndex: Int): Result<Unit>
}
