package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.QuizCreationInfo
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.quiz.navigation.CreateQuizRoute
import javax.inject.Inject

data class CreateQuizUiState(
    val quizTitle: String = "",
    val quizDescription: String = "",
    val quizDate: String = "",
    val quizSolveTime: Float = 10f,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreateQuizSuccess: Boolean = false,
) {
    val isCreateQuizButtonEnabled: Boolean
        get() = quizTitle.isNotBlank() && quizDate.isNotBlank() && quizSolveTime > 0
}

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val categoryId: String = savedStateHandle.toRoute<CreateQuizRoute>().categoryId

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
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            quizRepository.createQuiz(
                QuizCreationInfo(
                    quizTitle = uiState.value.quizTitle,
                    quizDescription = uiState.value.quizDescription.takeIf { it.isNotBlank() },
                    quizDate = uiState.value.quizDate,
                    quizSolveTime = uiState.value.quizSolveTime.toInt(),
                ),
            )
                .onSuccess { quizId ->
                    Log.d("CreateQuizViewModel", quizId)
                    addQuizToCategory(quizId)
                }
                .onFailure {
                    Log.d("CreateQuizViewModel", "Failed to create quiz")
                    _uiState.update { it.copy(isLoading = false, errorMessage = it.errorMessage) }
                }
        }
    }

    fun addQuizToCategory(quizId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            categoryRepository.addQuiz(categoryId, quizId)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isCreateQuizSuccess = true) }
                }
                .onFailure {
                    Log.d("CreateQuizViewModel", "Failed to add quiz to category")
                    _uiState.update { it.copy(isLoading = false, errorMessage = it.errorMessage) }
                }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}