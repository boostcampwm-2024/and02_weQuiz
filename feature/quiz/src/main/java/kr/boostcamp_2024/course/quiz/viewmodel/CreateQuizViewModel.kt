package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
    val snackBarMessage: String? = null,
    val isEditing: Boolean = false,
    val isEditQuizSuccess: Boolean = false,
    val selectedQuizTypeIndex: Int = 0,
) {
    val isCreateQuizButtonEnabled: Boolean
        get() = quizTitle.isNotBlank() && (isRealtimeQuiz || (quizDate.isNotBlank() && quizSolveTime > 0))
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

    private fun loadQuiz() {
        viewModelScope.launch {
            quizId?.let {
                quizRepository.getQuiz(it)
                    .onSuccess { quiz ->
                        when (quiz) {
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
                    .onFailure { throwable ->
                        Log.e("CreateQuizViewModel", throwable.message, throwable)
                        _uiState.update { state ->
                            state.copy(isLoading = false)
                        }
                        setNewSnackBarMessage("퀴즈 정보 불러오기에 실패했습니다.")
                    }
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
        setLoadingState()
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (uiState.value.isRealtimeQuiz) {
                createRealtimeQuiz()
            } else {
                createNormalQuiz()
            }
        }
    }

    private fun createNormalQuiz() {
        viewModelScope.launch {
            quizRepository.createQuiz(
                QuizCreationInfo(
                    quizTitle = uiState.value.quizTitle,
                    quizDescription = uiState.value.quizDescription.takeIf { it.isNotBlank() },
                    quizDate = uiState.value.quizDate,
                    quizSolveTime = uiState.value.quizSolveTime.toInt(),
                    quizImageUrl = uiState.value.currentImage?.let { imageByteArray ->
                        storageRepository.uploadImage(imageByteArray).getOrNull()
                    },
                ),
                ownerId = null,
            ).onSuccess { quizId ->
                saveQuizToCategory(quizId)
            }.onFailure {
                Log.e("CreateQuizViewModel", it.message, it)
                setNewSnackBarMessage("퀴즈 생성에 실패했습니다.")
            }
        }
    }

    private fun createRealtimeQuiz() {
        viewModelScope.launch {
            authRepository.getUserKey().onSuccess { userId ->
                quizRepository.createQuiz(
                    QuizCreationInfo(
                        quizTitle = uiState.value.quizTitle,
                        quizDescription = uiState.value.quizDescription.takeIf { it.isNotBlank() },
                        quizDate = uiState.value.quizDate,
                        quizSolveTime = uiState.value.quizSolveTime.toInt(),
                        quizImageUrl = uiState.value.currentImage?.let { imageByteArray ->
                            storageRepository.uploadImage(imageByteArray).getOrNull()
                        },
                    ),
                    ownerId = userId,
                ).onSuccess { quizId ->
                    saveQuizToCategory(quizId)
                }.onFailure {
                    Log.e("CreateQuizViewModel", it.message, it)
                    setNewSnackBarMessage("퀴즈 생성에 실패했습니다.")
                }
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

    fun editQuiz() {
        quizId?.let { id ->
            setLoadingState()
            viewModelScope.launch {
                val downloadUrl = uiState.value.currentImage?.let { imageByteArray ->
                    uiState.value.defaultImageUrl?.let { defaultUri ->
                        storageRepository.deleteImage(defaultUri)
                    }
                    storageRepository.uploadImage(imageByteArray).getOrNull()
                } ?: uiState.value.defaultImageUrl

                val quizCreationInfo = QuizCreationInfo(
                    quizTitle = uiState.value.quizTitle,
                    quizDescription = uiState.value.quizDescription.takeIf { it.isNotBlank() },
                    quizDate = uiState.value.quizDate,
                    quizSolveTime = uiState.value.quizSolveTime.toInt(),
                    quizImageUrl = downloadUrl,
                )

                quizRepository.editQuiz(id, quizCreationInfo)
                    .onSuccess {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                isEditQuizSuccess = true,
                            )
                        }
                    }
                    .onFailure {
                        Log.e("CreateQuizViewModel", it.message, it)
                        setNewSnackBarMessage("퀴즈 수정에 실패했습니다.")
                    }
            }
        }
    }

    fun setNewSnackBarMessage(message: String?) {
        _uiState.update { currentState -> currentState.copy(snackBarMessage = message) }
    }

    fun shownErrorMessage() {
        _uiState.update { it.copy(snackBarMessage = null) }
    }

    fun changeCurrentStudyImage(bytes: ByteArray) {
        _uiState.update { it.copy(currentImage = bytes) }

    }

}
