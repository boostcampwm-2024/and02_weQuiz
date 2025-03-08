package kr.boostcamp_2024.course.quiz.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.QuizNotFoundException
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.quiz.navigation.QuizRoute
import javax.inject.Inject

data class QuizUiState(
    val isLoading: Boolean = false,
    val category: Category? = null,
    val quiz: BaseQuiz? = null,
    val currentUserId: String? = null,
    val isDeleteQuizSuccess: Boolean = false,
    val isCancelWaitingRealTimeQuizSuccess: Boolean = false,
)

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val categoryRepository: CategoryRepository,
    private val quizRepository: QuizRepository,
    private val userOmrRepository: UserOmrRepository,
    private val questionRepository: QuestionRepository,
    private val storageRepository: StorageRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val quizRoute = savedStateHandle.toRoute<QuizRoute>()
    private val categoryId = quizRoute.categoryId
    private val quizId = quizRoute.quizId

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        initViewModel()
    }

    private fun initViewModel() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val userId = authRepository.getUserKey()
                val category = categoryRepository.getCategory(categoryId)
                val quizFlow = quizRepository.observeQuiz(quizId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUserId = userId,
                        category = category,
                    )
                }

                quizFlow.catch {
                    if (it !is QuizNotFoundException) _errorFlow.emit(it)
                }.collect { quiz ->
                    _uiState.update { it.copy(quiz = quiz) }
                }

            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun waitingRealTimeQuiz(waiting: Boolean) {
        if (uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                uiState.value.currentUserId?.let { currentUserId ->
                    uiState.value.quiz?.let { quiz ->
                        _uiState.update { it.copy(isLoading = true) }

                        quizRepository.waitingRealTimeQuiz(quiz.id, waiting, currentUserId)

                        when (waiting) {
                            true -> _uiState.update { it.copy(isLoading = false) }
                            false -> _uiState.update { it.copy(isLoading = false, isCancelWaitingRealTimeQuizSuccess = true) }
                        }
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun startRealTimeQuiz() {
        if (uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                uiState.value.quiz?.let { quiz ->
                    _uiState.update { it.copy(isLoading = true) }
                    quizRepository.startRealTimeQuiz(quiz.id)
                    _uiState.update { it.copy(isLoading = false) }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteQuiz(categoryId: String, quiz: BaseQuiz) {
        if (uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                categoryRepository.deleteQuizFromCategory(categoryId = categoryId, quizId = quiz.id)
                questionRepository.deleteQuestions(quiz.questions)
                userOmrRepository.deleteUserOmr(quiz.id)
                quizRepository.deleteQuiz(quiz.id)
                quiz.quizImageUrl?.let { quizImageUrl ->
                    storageRepository.deleteImage(quizImageUrl)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isDeleteQuizSuccess = true,
                    )
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
