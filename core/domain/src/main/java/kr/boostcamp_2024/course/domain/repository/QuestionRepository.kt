package kr.boostcamp_2024.course.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.boostcamp_2024.course.domain.model.BlankQuestionCreationInfo
import kr.boostcamp_2024.course.domain.model.ChoiceQuestionCreationInfo
import kr.boostcamp_2024.course.domain.model.Question

interface QuestionRepository {
    suspend fun getQuestions(questionIds: List<String>): List<Question>

    suspend fun getQuestion(questionId: String): Question

    fun observeQuestion(questionId: String): Flow<Question>

    suspend fun getRealTimeQuestions(questionIds: List<String>): List<Flow<Question>>

    suspend fun createQuestion(
        choiceQuestionCreationInfo: ChoiceQuestionCreationInfo,
    ): String

    suspend fun deleteQuestions(questionIds: List<String>)

    suspend fun createBlankQuestion(blankQuestionCreationInfo: BlankQuestionCreationInfo): String

    suspend fun updateCurrentSubmit(userId: String?, questionId: String, userAnswer: Any?)
}
