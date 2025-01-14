package kr.boostcamp_2024.course.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.boostcamp_2024.course.domain.model.BlankQuestionCreationInfo
import kr.boostcamp_2024.course.domain.model.ChoiceQuestionCreationInfo
import kr.boostcamp_2024.course.domain.model.Question

interface QuestionRepository {
    suspend fun getQuestions(questionIds: List<String>): Result<List<Question>>

    suspend fun getQuestion(questionId: String): Result<Question>

    fun observeQuestion(questionId: String): Flow<Question>

    suspend fun getRealTimeQuestions(questionIds: List<String>): Result<List<Flow<Question>>>

    suspend fun createQuestion(
        choiceQuestionCreationInfo: ChoiceQuestionCreationInfo,
    ): Result<String>

    suspend fun deleteQuestions(questionIds: List<String>): Result<Unit>

    suspend fun createBlankQuestion(blankQuestionCreationInfo: BlankQuestionCreationInfo): Result<String>

    suspend fun updateCurrentSubmit(userId: String?, questionId: String, userAnswer: Any?): Result<Unit>
}
