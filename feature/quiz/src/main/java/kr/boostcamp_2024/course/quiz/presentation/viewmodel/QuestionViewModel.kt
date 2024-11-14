package kr.boostcamp_2024.course.quiz.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.model.UserOmr
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.navigation.QuestionScreenRoute
import javax.inject.Inject

data class QuestionUiState(
    val quiz: Quiz? = null,
    val questions: List<Question> = emptyList(),
    val isSubmitting: Boolean = false,
    val currentPage: Int = 0,
    val selectedIndexList: List<Int> = List(10) { -1 },
    val showDialog: Boolean = false,
    val countDownTime: Int = 20 * 60,
    val isLoading: Boolean = false,
    val errorMessageId: Int? = null,
    val userOmr: UserOmr? = null,
)

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val quizRepository: QuizRepository,
    private val userOmrRepository: UserOmrRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val quizId = savedStateHandle.toRoute<QuestionScreenRoute>().quizId
    private val _uiState: MutableStateFlow<QuestionUiState> = MutableStateFlow(QuestionUiState())
    val uiState: StateFlow<QuestionUiState> = _uiState
        .onStart {
            initial()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            QuestionUiState(),
        )

    private fun initial() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            quizRepository.getQuiz(quizId)
                .onSuccess { quiz ->
                    _uiState.update { it.copy(isLoading = false, quiz = quiz) }
                    loadQuestions(quiz.questions)
                }
                .onFailure {
                    Log.e("QuestionViewModel", "퀴즈 로드 실패", it)
                    _uiState.update { it.copy(isLoading = false, errorMessageId = R.string.err_load_quiz) }
                }
            updateTimer()
        }
    }

    private fun loadQuestions(questionIds: List<String>) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            questionRepository.getQuestions(questionIds)
                .onSuccess { questions ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            questions = questions,
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    Log.e("QuestionViewModel", "문제 로드 실패", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.err_load_questions,
                        )
                    }
                }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessageId = null)
        }
    }

    fun selectOption(pageIndex: Int, optionIndex: Int) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedIndexList = currentState.selectedIndexList.toMutableList().apply {
                    this[pageIndex] = optionIndex
                },
            )
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

    fun toggleDialog(show: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(showDialog = show)
        }
    }

    fun updateTimer() {
        viewModelScope.launch {
            while (_uiState.value.countDownTime > 0) {
                kotlinx.coroutines.delay(1000L)
                _uiState.update { currentState ->
                    currentState.copy(countDownTime = currentState.countDownTime - 1)
                }
            }
        }
    }

    fun submitAnswers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            _uiState.value.userOmr?.copy(
                userId = "M2PzD8bxVaDAwNrLhr6E",
                quizId = quizId,
                answers = _uiState.value.selectedIndexList.map { it },
            )?.let { userOmr ->
                userOmrRepository.submitQuiz(userOmr)
                    .onSuccess { userOmrId ->
                        quizRepository.addUserOmrToQuiz(userOmr.quizId, userOmrId)
                            .onSuccess {
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        isSubmitting = false,
                                        userOmr = userOmr.copy(id = userOmrId),
                                    )
                                }
                            }
                            .onFailure {
                                Log.e("QuestionViewModel", "userOmrId 업데이트 실패", it)
                                _uiState.update { currentState ->
                                    currentState.copy(
                                        isSubmitting = false,
                                        errorMessageId = R.string.err_answer_add,
                                    )
                                }
                            }
                    }
                    .onFailure {
                        Log.e("QuestionViewModel", "퀴즈 정답 제출 실패", it)
                        _uiState.update {
                            it.copy(
                                isSubmitting = false,
                                errorMessageId = R.string.err_answer_add_quiz,
                            )
                        }
                    }
            }

        }
    }
}
