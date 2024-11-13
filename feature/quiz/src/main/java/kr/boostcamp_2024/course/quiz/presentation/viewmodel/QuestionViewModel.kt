package kr.boostcamp_2024.course.quiz.presentation.viewmodel

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
import kr.boostcamp_2024.course.domain.model.UserOmr
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

data class QuestionUiState(
    val questions: List<Question> = emptyList(),
    val isSubmitting: Boolean = false,
    val currentPage: Int = 0,
    val selectedIndexList: List<Int> = List(10) { -1 },
    val showDialog: Boolean = false,
    val countDownTime: Int = 20,
    val isLoading: Boolean = false,
    val errorMessage: String = "",
    val userOmr: UserOmr = UserOmr(
        userId = "BcnL7sXFxXBigOVNSUhQ",
        quizId = "2k1QrCuOUHLERgQAmMqg",
        answers = List(10) { -1 },
    ),
)

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<QuestionUiState> = MutableStateFlow(QuestionUiState())
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

    fun initialize(solveTime: Int, questionIds: List<String>) {
        _uiState.value = _uiState.value.copy(countDownTime = solveTime)
        loadQuestions(questionIds)
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
                    Log.e("MainViewModel", "Failed to load study groups", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "스터디 그룹 로드에 실패했습니다.",
                        )
                    }
                }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessage = "")
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
            val selectedAnswers = _uiState.value.selectedIndexList
            Log.d("QuestionViewModel", "Selected answers: $selectedAnswers") // 정답 리스트 로깅

            val userOmr = _uiState.value.userOmr.copy(
                answers = _uiState.value.selectedIndexList.map{ it + 1 }
            )

            _uiState.update { it.copy(isSubmitting = true) }

            questionRepository.submitQuiz(userOmr)
                .onSuccess { userOmrId ->
                    questionRepository.addUserOmrToQuiz(userOmr.quizId, userOmrId)
                        .onSuccess {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isSubmitting = false,
                                    userOmr = userOmr.copy(userId = userOmrId)
                                )
                            }
                        }
                        .onFailure {
                            Log.e("QuestionViewModel", "userOmrId 업데이트 실패", it)
                            _uiState.update { currentState ->
                                currentState.copy(
                                    isSubmitting = false,
                                    errorMessage = "퀴즈에 응답 추가 실패했습니다."
                                )
                            }
                        }
                }
                .onFailure {
                    Log.e("QuestionViewModel", "퀴즈 정답 제출 실패", it)
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = "응답 제출에 실패했습니다."
                        )
                    }
                }
        }
    }
}
