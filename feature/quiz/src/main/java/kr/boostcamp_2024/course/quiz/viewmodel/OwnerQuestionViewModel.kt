package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.quiz.R
import javax.inject.Inject

data class QuestionUiState(
    val quiz: RealTimeQuiz? = null,
    val questions: List<Question?> = emptyList(),
    val ownerName: String? = null,
    val isSubmitting: Boolean = false,
    val currentPage: Int = 0,
    val selectedIndexList: List<Int> = emptyList(),
    val countDownTime: Int = 20 * 60,
    val isLoading: Boolean = false,
    val errorMessageId: Int? = null,
    val currentUserId: String? = null,
    val userOmrId: String? = null,
)

@HiltViewModel
class OwnerQuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<QuestionUiState> = MutableStateFlow(QuestionUiState())
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

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
                        launch {
                            questionFlow.collect { question ->
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
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }.onFailure {
                    Log.e("OwnerQuestionViewModel", "Failed to load real time questions", it)
                    showErrorMessage(R.string.err_load_questions)
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
}
