package kr.boostcamp_2024.course.quiz.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.model.QuizCreationInfo
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import kr.boostcamp_2024.course.quiz.navigation.CreateQuizRoute
import javax.inject.Inject

data class CreateQuizUiState(
    val quizTitle: String = "",
    val quizDescription: String = "",
    val quizDate: String = "",
    val quizSolveTime: Float = 10f,
    val defaultImageUrl: String? = null,
    val currentImage: ByteArray? = null,
    val isCreateQuizSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isEditing: Boolean = false,
    val isEditQuizSuccess: Boolean = false,
    val selectedQuizTypeIndex: Int = 0,
) {
    val isCreateQuizButtonEnabled: Boolean
        get() = quizTitle.length in 1..20 && quizDescription.length in 0..100 && (isRealtimeQuiz || (quizDate.isNotBlank() && quizSolveTime > 0))
    val isRealtimeQuiz: Boolean
        get() = selectedQuizTypeIndex == 1
}

@HiltViewModel
class CreateQuizViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val quizRepository: QuizRepository,
    private val categoryRepository: CategoryRepository,
    private val storageRepository: StorageRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val categoryId: String = savedStateHandle.toRoute<CreateQuizRoute>().categoryId
    private val quizId: String? = savedStateHandle.toRoute<CreateQuizRoute>().quizId

    private val _uiState = MutableStateFlow(CreateQuizUiState())
    val uiState = _uiState.asStateFlow()
        .onStart {
            loadQuiz()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            CreateQuizUiState(),
        )

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    private fun loadQuiz() {
        viewModelScope.launch {
            try {
                quizId?.let {
                    setLoadingState(true)

                    when (val quiz = quizRepository.getQuiz(it)) {
                        is Quiz -> {
                            _uiState.update { state ->
                                state.copy(
                                    quizTitle = quiz.title,
                                    quizDescription = quiz.description ?: "",
                                    quizDate = quiz.startTime,
                                    quizSolveTime = quiz.solveTime.toFloat(),
                                    isEditing = true,
                                    isLoading = false,
                                    defaultImageUrl = quiz.quizImageUrl,
                                    selectedQuizTypeIndex = 0,
                                )
                            }
                        }

                        is RealTimeQuiz -> {
                            _uiState.update { state ->
                                state.copy(
                                    quizTitle = quiz.title,
                                    quizDescription = quiz.description ?: "",
                                    isEditing = true,
                                    isLoading = false,
                                    defaultImageUrl = quiz.quizImageUrl,
                                    selectedQuizTypeIndex = 1,
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoadingState(false)
            }
        }
    }

    fun setSelectedQuizTypeIndex(index: Int) {
        if (_uiState.value.isEditing) return
        _uiState.update { it.copy(selectedQuizTypeIndex = index) }
    }

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
            try {
                setLoadingState(true)

                if (uiState.value.isRealtimeQuiz) {
                    createRealtimeQuiz()
                } else {
                    createGeneralQuiz()
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoadingState(false)
            }
        }
    }

    private suspend fun createGeneralQuiz() {
        val quizId = quizRepository.createQuiz(
            QuizCreationInfo(
                quizTitle = uiState.value.quizTitle,
                quizDescription = uiState.value.quizDescription.takeIf { it.isNotBlank() },
                quizDate = uiState.value.quizDate,
                quizSolveTime = uiState.value.quizSolveTime.toInt(),
                quizImageUrl = uiState.value.currentImage?.let { imageByteArray ->
                    storageRepository.uploadImage(imageByteArray)
                },
            ),
            ownerId = null,
        )

        categoryRepository.addQuizToCategory(categoryId, quizId)

        _uiState.update {
            it.copy(
                isLoading = false,
                isCreateQuizSuccess = true,
            )
        }
    }

    private suspend fun createRealtimeQuiz() {
        val userId = authRepository.getUserKey()
        val quizId = quizRepository.createQuiz(
            QuizCreationInfo(
                quizTitle = uiState.value.quizTitle,
                quizDescription = uiState.value.quizDescription.takeIf { it.isNotBlank() },
                quizDate = uiState.value.quizDate,
                quizSolveTime = uiState.value.quizSolveTime.toInt(),
                quizImageUrl = uiState.value.currentImage?.let { imageByteArray ->
                    storageRepository.uploadImage(imageByteArray)
                },
            ),
            ownerId = userId,
        )

        categoryRepository.addQuizToCategory(categoryId, quizId)

        _uiState.update {
            it.copy(
                isLoading = false,
                isCreateQuizSuccess = true,
            )
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoading = isLoading,
            )
        }
    }

    fun editQuiz() {
        viewModelScope.launch {
            try {
                quizId?.let { id ->
                    setLoadingState(true)

                    val downloadUrl = uiState.value.currentImage?.let { imageByteArray ->
                        uiState.value.defaultImageUrl?.let { defaultUri ->
                            storageRepository.deleteImage(defaultUri)
                        }
                        storageRepository.uploadImage(imageByteArray)
                    } ?: uiState.value.defaultImageUrl

                    val quizCreationInfo = QuizCreationInfo(
                        quizTitle = uiState.value.quizTitle,
                        quizDescription = uiState.value.quizDescription.takeIf { it.isNotBlank() },
                        quizDate = uiState.value.quizDate,
                        quizSolveTime = uiState.value.quizSolveTime.toInt(),
                        quizImageUrl = downloadUrl,
                    )

                    quizRepository.editQuiz(id, quizCreationInfo, _uiState.value.selectedQuizTypeIndex)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEditQuizSuccess = true,
                        )
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoadingState(false)
            }
        }
    }

    fun changeCurrentStudyImage(bytes: ByteArray) {
        _uiState.update { it.copy(currentImage = bytes) }

    }

}
