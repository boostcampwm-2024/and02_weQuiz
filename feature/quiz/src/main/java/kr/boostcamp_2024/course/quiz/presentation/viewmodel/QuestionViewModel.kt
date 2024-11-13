package kr.boostcamp_2024.course.quiz.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Question
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
            val result = questionRepository.getQuestions(questionIds)
            _uiState.value = result.getOrDefault(emptyList()).let { questions ->
                _uiState.value.copy(
                    selectedIndexList = List(questions.size) { -1 },
                    questions = questions,
                    isLoading = false,
                )
            }
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

    fun submitOrExit(onQuizFinished: () -> Unit, onNavigationButtonClick: () -> Unit) {
        viewModelScope.launch {
            // TODO: 제출 로직 또는 나가기 로직
            onQuizFinished() // 또는 onNavigationButtonClick()
        }
    }
}
