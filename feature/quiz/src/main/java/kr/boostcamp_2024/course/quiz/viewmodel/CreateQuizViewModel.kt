package kr.boostcamp_2024.course.quiz.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import javax.inject.Inject

data class CreateQuizUiState(
    val quizTitle: String = "",
    val quizDescription: String = "",
    val quizDate: String = "",
    val quizSolveTime: Float = 0f,
    val isCreateQuizSuccess: Boolean = false,
)

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateQuizUiState())
    val uiState = _uiState.asStateFlow()

    fun setQuizTitle(quizTitle: String) {
        _uiState.update { it.copy(quizTitle = quizTitle) }
    }

    fun setQuizDescription(quizDescription: String) {
        _uiState.update { it.copy(quizDescription = quizDescription) }
    }

    fun setQuizDate(quizDate: String) {
        _uiState.update { it.copy(quizDate = quizDate) }
    }

    fun setQuizSolveTime(quizSolveTime: Float) {
        _uiState.update { it.copy(quizSolveTime = quizSolveTime) }
    }

    fun createQuiz() {
        _uiState.update { it.copy(isCreateQuizSuccess = true) }
    }
}