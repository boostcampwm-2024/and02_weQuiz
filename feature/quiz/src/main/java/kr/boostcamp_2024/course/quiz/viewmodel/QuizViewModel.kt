package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.Category
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
    val errorMessage: String? = null,
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

    init {
        loadCurrentUserId()
        loadCategory()
        loadQuiz()
    }

    private fun loadCurrentUserId() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authRepository.getUserKey()
                .onSuccess { userId ->
                    _uiState.update { it.copy(isLoading = false, currentUserId = userId) }
                }
                .onFailure {
                    Log.e("QuizViewModel", "Failed to load current user", it)
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "로그인이 필요합니다.")
                    }
                }
        }
    }

    private fun loadCategory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            categoryRepository.getCategory(categoryId)
                .onSuccess { category ->
                    _uiState.update { it.copy(isLoading = false, category = category) }
                }
                .onFailure {
                    Log.e("QuizViewModel", "Failed to load category", it)
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "카테고리 로드에 실패했습니다.")
                    }
                }
        }
    }

    private fun loadQuiz() {
        viewModelScope.launch {
            quizRepository.observeQuiz(quizId)
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch {
                    Log.e("QuizViewModel", "Failed to observe quiz", it)
                    _uiState.update { it.copy(isLoading = false, errorMessage = "퀴즈 로드에 실패했습니다.") }
                }
                .collect { quiz ->
                    _uiState.update { it.copy(isLoading = false, quiz = quiz) }
                }
        }
    }

    fun waitingRealTimeQuiz(waiting: Boolean) {
        if (uiState.value.isLoading) return

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            uiState.value.currentUserId?.let { currentUserId ->
                uiState.value.quiz?.let { quiz ->
                    quizRepository.waitingRealTimeQuiz(quiz.id, waiting, currentUserId)
                        .onSuccess {
                            when (waiting) {
                                true -> _uiState.update { it.copy(isLoading = false) }
                                false -> _uiState.update { it.copy(isLoading = false, isCancelWaitingRealTimeQuizSuccess = true) }
                            }
                        }
                        .onFailure {
                            Log.e("QuizViewModel", "Failed to waiting real-time quiz", it)
                            _uiState.update {
                                it.copy(isLoading = false, errorMessage = "실시간 퀴즈 대기 상태 변경에 실패했습니다.")
                            }
                        }
                }
            }
        }
    }

    fun startRealTimeQuiz() {
        if (uiState.value.isLoading) return

        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            uiState.value.quiz?.let { quiz ->
                quizRepository.startRealTimeQuiz(quiz.id)
                    .onSuccess {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                    .onFailure {
                        Log.e("QuizViewModel", "Failed to start real-time quiz", it)
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = "실시간 퀴즈 시작에 실패했습니다.")
                        }
                    }
            }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun deleteQuiz(categoryId: String, quiz: BaseQuiz) {
        if (uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            categoryRepository.deleteQuizFromCategory(categoryId = categoryId, quizId = quiz.id)
                .onSuccess {
                    questionRepository.deleteQuestions(quiz.questions)
                        .onSuccess {
                            userOmrRepository.deleteUserOmr(quiz.id)
                                .onSuccess {
                                    quizRepository.deleteQuiz(quiz.id)
                                        .onSuccess {
                                            quiz.quizImageUrl?.let { quizImageUrl ->
                                                storageRepository.deleteImage(quizImageUrl)
                                                    .onSuccess {
                                                        _uiState.update {
                                                            it.copy(
                                                                isLoading = false,
                                                                isDeleteQuizSuccess = true,
                                                            )
                                                        }
                                                    }
                                                    .onFailure {
                                                        Log.e(
                                                            "QuizViewModel",
                                                            "Failed to delete quiz image",
                                                            it,
                                                        )
                                                        _uiState.update {
                                                            it.copy(
                                                                isLoading = false,
                                                                errorMessage = "퀴즈 이미지 삭제에 실패하였습니다.",
                                                            )
                                                        }
                                                    }
                                            } ?: _uiState.update {
                                                it.copy(
                                                    isLoading = false,
                                                    isDeleteQuizSuccess = true,
                                                )
                                            }

                                        }
                                        .onFailure {
                                            Log.e("QuizViewModel", "Failed to delete quiz", it)
                                            _uiState.update {
                                                it.copy(
                                                    isLoading = false,
                                                    errorMessage = "퀴즈 삭제에 실패했습니다.",
                                                )
                                            }
                                        }
                                }
                                .onFailure {
                                    Log.e("QuizViewModel", "Failed to delete userOmr", it)
                                    _uiState.update {
                                        it.copy(
                                            isLoading = false,
                                            errorMessage = "답안 삭제에 실패했습니다.",
                                        )
                                    }
                                }
                        }.onFailure {
                            Log.e("QuizViewModel", "Failed to delete questions", it)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = "퀴즈 문제 삭제에 실패했습니다.",
                                )
                            }
                        }
                }
                .onFailure {
                    Log.e("QuizViewModel", "Failed to delete quiz from category", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "카테고리에서 퀴즈를 삭제하는데 실패했습니다.",
                        )
                    }
                }
        }
    }
}
