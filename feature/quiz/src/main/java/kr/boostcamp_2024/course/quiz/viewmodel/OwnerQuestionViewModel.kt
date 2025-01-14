package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.BlankQuestionManager
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.quiz.R
import javax.inject.Inject

data class RealTimeWithOwnerQuestionUiState(
    val quiz: RealTimeQuiz? = null,
    val questions: List<Question?> = emptyList(),
    val ownerName: String? = null,
    val currentPage: Int = 0,
    val isLoading: Boolean = false,
    val errorMessageId: Int? = null,
    val currentUserId: String? = null,
    val isQuizFinished: Boolean = false,
    val blankQuestionContents: List<Map<String, Any>?> = emptyList(),
    val blankWords: List<Map<String, Any>> = emptyList(),
)

@HiltViewModel
class OwnerQuestionViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<RealTimeWithOwnerQuestionUiState> = MutableStateFlow(RealTimeWithOwnerQuestionUiState())
    val uiState: StateFlow<RealTimeWithOwnerQuestionUiState> = _uiState.asStateFlow()
    val blankQuestionManager = BlankQuestionManager(::setNewBlankContents)

    fun initQuizData(quiz: RealTimeQuiz?, currentUserId: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                quiz = quiz,
                currentUserId = currentUserId,
            )
        }
        quiz?.let {
            getQuizOwnerName(quiz.ownerId)
            loadRealTimeQuestions(quiz.questions)
        }
    }

    private fun getQuizOwnerName(ownerId: String) {
        viewModelScope.launch {
            userRepository.getUser(ownerId)
                .onSuccess { user ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            ownerName = user.name,
                        )
                    }
                }
                .onFailure {
                    Log.e("OwnerQuestionViewModel", "Failed to get quiz owner name", it)
                    showErrorMessage(R.string.err_load_owner_name)
                }
        }
    }

    private fun loadRealTimeQuestions(questionIds: List<String>) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    questions = List(questionIds.size) { null },
                )
            }
            questionRepository.getRealTimeQuestions(questionIds)
                .onSuccess { questionList ->
                    questionList.forEachIndexed { index, questionFlow ->
                        viewModelScope.launch {
                            questionFlow
                                .catch {
                                    Log.e("OwnerQuestionViewModel", "Failed to load real time questions", it)
                                    showErrorMessage(R.string.err_load_questions)
                                }
                                .collect { question ->
                                    _uiState.update { currentState ->
                                        currentState.copy(
                                            questions = currentState.questions.toMutableList().apply {
                                                this[index] = question
                                            },
                                        )
                                    }
                                }
                        }
                    }
                    setLoadingState(false)
                }.onFailure {
                    Log.e("OwnerQuestionViewModel", "Failed to load real time questions", it)
                    showErrorMessage(R.string.err_load_questions)
                }
        }
    }

    fun setQuizFinished() {
        viewModelScope.launch {
            setLoadingState(true)
            val currentQuizId = requireNotNull(_uiState.value.quiz?.id)
            quizRepository.setQuizFinished(currentQuizId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isQuizFinished = true,
                            isLoading = false,
                        )
                    }
                }.onFailure {
                    Log.e("OwnerQuestionViewModel", "Failed to set quiz finished", it)
                    showErrorMessage(R.string.error_quiz_finished)
                    setLoadingState(false)
                }
        }
    }

    fun showErrorMessage(errorMessageId: Int?) {
        _uiState.update { currentState ->
            currentState.copy(
                errorMessageId = errorMessageId,
            )
        }
    }

    fun shownErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessageId = null)
        }
    }

    fun nextPage() {
        updateCurrentPage(_uiState.value.currentPage + 1)
    }

    fun previousPage() {
        updateCurrentPage(_uiState.value.currentPage - 1)
    }

    private fun updateCurrentPage(pageIdx: Int) {
        val currentQuizId = requireNotNull(_uiState.value.quiz?.id)
        viewModelScope.launch {
            quizRepository.updateQuizCurrentQuestion(currentQuizId, pageIdx)
                .onSuccess {
                    _uiState.update {
                        it.copy(currentPage = pageIdx)
                    }
                    setNewBlankQuestionManager(pageIdx)
                }.onFailure {
                    Log.e("OwnerQuestionViewModel", "Failed to update current page", it)
                    showErrorMessage(R.string.err_update_current_question)
                }
        }
    }

    private fun setNewBlankQuestionManager(pageIdx: Int) {
        val currentQuestion = _uiState.value.questions[pageIdx]
        if (currentQuestion is BlankQuestion) {
            blankQuestionManager.setNewQuestions(
                isOwner = true,
                questionContents = currentQuestion.questionContent,
            )
            setNewBlankContents()
        }
    }

    private fun setNewBlankContents() {
        _uiState.update {
            it.copy(
                blankQuestionContents = blankQuestionManager.contents,
                blankWords = blankQuestionManager.blankWords,
            )
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = isLoading)
        }
    }
}
