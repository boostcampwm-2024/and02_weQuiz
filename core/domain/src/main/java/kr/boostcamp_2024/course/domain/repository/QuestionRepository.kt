package kr.boostcamp_2024.course.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestionCreationInfo

interface QuestionRepository {
    suspend fun getQuestions(questionIds: List<String>): Result<List<ChoiceQuestion>>

    suspend fun getQuestion(questionId: String): Result<ChoiceQuestion>

    fun observeQuestion(questionId: String): Flow<ChoiceQuestion>

    suspend fun getRealTimeQuestions(questionIds: List<String>): Result<List<Flow<ChoiceQuestion>>>

    suspend fun createQuestion(
        choiceQuestionCreationInfo: ChoiceQuestionCreationInfo,
    ): Result<String>

    suspend fun deleteQuestions(questionIds: List<String>): Result<Unit>

    suspend fun updateCurrentSubmit(questionId: String, selectedIndex: Int): Result<Unit>
}
