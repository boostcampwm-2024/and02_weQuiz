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
    val isCreateQuizSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val snackBarMessage: String? = null,
) {
    val isCreateQuizButtonEnabled: Boolean
        get() = quizTitle.isNotBlank() && quizDate.isNotBlank() && quizSolveTime > 0
}

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val quizRepository: QuizRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateQuizUiState())
    val uiState = _uiState.asStateFlow()
    private val categoryId = savedStateHandle.toRoute<CreateQuizRoute>().categoryId

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
        setLoadingState()
        viewModelScope.launch {
            quizRepository.createQuiz(
                QuizCreationInfo(
                    quizTitle = uiState.value.quizTitle,
                    quizDescription = uiState.value.quizDescription.takeIf { it.isNotBlank() },
                    quizDate = uiState.value.quizDate,
                    quizSolveTime = uiState.value.quizSolveTime.toInt(),
                ),
            ).onSuccess { quizId ->
                saveQuizToCategory(quizId)
            }.onFailure {
                Log.e("CreateQuizViewModel", it.message, it)
                setNewSnackBarMessage("퀴즈 생성에 실패했습니다.")
            }
        }
    }

    private suspend fun saveQuizToCategory(quizId: String) {
        try {
            categoryRepository.addQuizToCategory(categoryId, quizId).getOrThrow()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    isCreateQuizSuccess = true,
                )
            }
        } catch (exception: Exception) {
            Log.e("CreateQuizViewModel", exception.message, exception)
            setNewSnackBarMessage("퀴즈 저장에 실패했습니다.")
        }
    }

    private fun setLoadingState() {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true,
            )
        }
    }

    fun setNewSnackBarMessage(message: String?) {
        _uiState.update { currentState ->
            currentState.copy(
                snackBarMessage = message,
            )
        }
    }
}
