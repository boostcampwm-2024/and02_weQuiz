package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.quiz.R
import javax.inject.Inject

data class RealTimeWithOwnerQuestionUiState(
    val quiz: RealTimeQuiz? = null,
    val choiceQuestions: List<Question?> = emptyList(),
    val ownerName: String? = null,
    val currentPage: Int = 0,
    val isLoading: Boolean = false,
    val errorMessageId: Int? = null,
    val currentUserId: String? = null,
    val isQuizFinished: Boolean = false,
)

@HiltViewModel
class OwnerQuestionViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<RealTimeWithOwnerQuestionUiState> = MutableStateFlow(RealTimeWithOwnerQuestionUiState())
    val uiState: StateFlow<RealTimeWithOwnerQuestionUiState> = _uiState.asStateFlow()

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
                    choiceQuestions = List(questionIds.size) { null },
                )
            }
            questionRepository.getRealTimeQuestions(questionIds)
                .onSuccess { questionList ->
                    questionList.forEachIndexed { index, questionFlow ->
                        viewModelScope.launch {
                            questionFlow.onEach { question ->
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        choiceQuestions = currentState.choiceQuestions.toMutableList().apply {
                                            this[index] = question
                                        },
                                    )
                                }
                            }.catch {
                                Log.e("OwnerQuestionViewModel", "Failed to load real time questions", it)
                                showErrorMessage(R.string.err_load_questions)
                            }.collect()
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
        _uiState.update { currentState ->
            currentState.copy(currentPage = currentState.currentPage + 1)
        }
    }

    fun previousPage() {
        _uiState.update { currentState ->
            currentState.copy(currentPage = currentState.currentPage - 1)
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = isLoading)
        }
    }
}
