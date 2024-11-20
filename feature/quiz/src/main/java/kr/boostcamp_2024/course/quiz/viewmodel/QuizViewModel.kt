package kr.boostcamp_2024.course.quiz.viewmodel

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
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.quiz.navigation.QuizRoute
import javax.inject.Inject

data class QuizUiState(
    val isLoading: Boolean = false,
    val category: Category? = null,
    val quiz: BaseQuiz? = null,
    val errorMessage: String? = null,
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository,
    private val quizRepository: QuizRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val quizRoute = savedStateHandle.toRoute<QuizRoute>()
    private val categoryId = quizRoute.categoryId
    private val quizId = quizRoute.quizId

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState
        .onStart {
            loadCategory()
            loadQuiz()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            QuizUiState(),
        )

    private fun loadCategory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            categoryRepository.getCategory(categoryId)
                .onSuccess { category ->
                    _uiState.update { it.copy(isLoading = false, category = category) }
                }
                .onFailure {
                    Log.e("QuizViewModel", "Failed to load category", it)
                    _uiState.update { it.copy(isLoading = false, errorMessage = "카테고리 로드에 실패했습니다.") }
                }
        }
    }

    private fun loadQuiz() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            quizRepository.getQuiz(quizId)
                .onSuccess { quiz ->
                    _uiState.update { it.copy(isLoading = false, quiz = quiz) }
                }
                .onFailure {
                    Log.e("QuizViewModel", "Failed to load quiz", it)
                    _uiState.update { it.copy(isLoading = false, errorMessage = "퀴즈 로드에 실패했습니다.") }
                }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
