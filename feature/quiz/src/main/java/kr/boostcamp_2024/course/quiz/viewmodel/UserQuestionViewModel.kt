package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.BlankQuestionManager
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.UserOmrCreationInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.navigation.QuestionRoute
import javax.inject.Inject

data class UserQuestionUiState(
    val quiz: BaseQuiz? = null,
    val questions: List<Question> = emptyList(),
    val currentPage: Int = 0,
    val selectedIndexList: List<Any?> = emptyList(),
    val submittedIndexList: List<Any?> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessageId: Int? = null,
    val currentUserId: String? = null,
    val userOmrId: String? = null,
    val isSubmitted: Boolean = false,
    val isQuizFinished: Boolean = false,
    val blankQuestionContents: List<Map<String, Any>?> = emptyList(),
    val blankWords: List<Map<String, Any>> = emptyList(),
    val isExitSuccess: Boolean = false,
)

@HiltViewModel
class UserQuestionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questionRepository: QuestionRepository,
    private val quizRepository: QuizRepository,
    private val userOmrRepository: UserOmrRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val quizId = savedStateHandle.toRoute<QuestionRoute>().quizId
    private val _uiState: MutableStateFlow<UserQuestionUiState> =
        MutableStateFlow(UserQuestionUiState())
    val blankQuestionManager = BlankQuestionManager(::setNewBlankContents)

    init {
        initial()
    }

    val uiState: StateFlow<UserQuestionUiState> = _uiState

    private fun initial() {
        viewModelScope.launch {
            loadCurrentUserId()

            _uiState.update { it.copy(isLoading = true) }
            quizRepository.getQuiz(quizId)
                .onSuccess { quiz ->
                    _uiState.update { it.copy(isLoading = false, quiz = quiz) }
                    loadQuestions(quiz.questions)
                }
                .onFailure {
                    Log.e("QuestionViewModel", "퀴즈 로드 실패", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.err_load_quiz,
                        )
                    }
                }
        }
    }

    private fun loadQuestions(questionIds: List<String>) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            questionRepository.getQuestions(questionIds)
                .onSuccess { questions ->
                    val baseSelectedList = questions.map {
                        if (it is BlankQuestion) {
                            mapOf<Int, String?>()
                        } else {
                            -1
                        }
                    }
                    _uiState.update { currentState ->

                        currentState.copy(
                            questions = questions,
                            selectedIndexList = baseSelectedList,
                            submittedIndexList = baseSelectedList,
                            isLoading = false,
                        )
                    }
                    updatePageAndSubmitByOwner()
                }
                .onFailure {
                    Log.e("QuestionViewModel", "문제 로드 실패", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.err_load_questions,
                        )
                    }
                }
        }
    }

    private fun loadCurrentUserId() {
        viewModelScope.launch {
            authRepository.getUserKey()
                .onSuccess { currentUser ->
                    _uiState.update { it.copy(currentUserId = currentUser) }
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to load current user", it)
                    _uiState.update { it.copy(errorMessageId = R.string.err_load_current_user) }
                }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { currentState ->
            currentState.copy(errorMessageId = null)
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

    fun selectBlanks(pageIndex: Int, blanks: Map<String, String?>) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedIndexList = currentState.selectedIndexList.toMutableList().apply {
                    this[pageIndex] = blanks
                },
            )
        }
    }

    private fun updatePageAndSubmitByOwner() {
        viewModelScope.launch {
            quizRepository.observeRealTimeQuiz(quizId)
                .collect { result ->
                    result
                        .onSuccess { quiz ->
                            _uiState.update {
                                setNewBlankQuestionManager(quiz.currentQuestion)
                                it.copy(
                                    currentPage = quiz.currentQuestion,
                                    isSubmitted = false,
                                    isQuizFinished = quiz.isFinished,
                                )
                            }
                        }.onFailure {
                            Log.e("UserQuestionViewModel", "페이지 로드 실패", it)
                            _uiState.update { it.copy(errorMessageId = R.string.err_load_current_page) }
                        }
                }
        }
    }

    private fun setNewBlankQuestionManager(pageIdx: Int) {
        val currentQuestion = _uiState.value.questions[pageIdx]
        if (currentQuestion is BlankQuestion) {
            blankQuestionManager.setNewQuestions(
                questionContents = currentQuestion.questionContent,
            )
            setNewBlankContents()
        }
    }

    private fun setNewBlankContents() {
        _uiState.update {
            it.copy(
                blankQuestionContents = blankQuestionManager.contents,
                blankWords = blankQuestionManager.blankWords,
            )
        }
    }

    fun submitQuestion(questionId: String) {
        val currentState = _uiState.value
        val currentUser = currentState.currentUserId ?: return
        viewModelScope.launch {
            val result = questionRepository.updateCurrentSubmit(
                currentUser,
                questionId,
                _uiState.value.selectedIndexList[_uiState.value.currentPage],
            )
            val updatedList = _uiState.value.submittedIndexList.toMutableList().apply {
                this[_uiState.value.currentPage] =
                    _uiState.value.selectedIndexList[_uiState.value.currentPage]
            }
            result.onSuccess {
                _uiState.update { it.copy(isSubmitted = true, submittedIndexList = updatedList) }
            }.onFailure {
                Log.e("UserQuestionViewModel", "제출 실패 ", it)
                _uiState.update { it.copy(errorMessageId = R.string.err_submit_quetion) }
            }
        }
    }

    fun submitAnswers() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                _uiState.value.currentUserId?.let { userId ->
                    val userOmrCreationInfo = UserOmrCreationInfo(
                        userId = userId,
                        quizId = quizId,
                        answers = _uiState.value.submittedIndexList as List<Any>,
                    )
                    userOmrRepository.submitQuiz(userOmrCreationInfo)
                        .onSuccess { userOmrId ->
                            quizRepository.addUserOmrToQuiz(quizId, userOmrId)
                                .onSuccess {
                                    _uiState.update {
                                        it.copy(
                                            userOmrId = userOmrId,
                                            isLoading = false,
                                        )
                                    }
                                }
                                .onFailure {
                                    Log.e("QuestionViewModel", "userOmrId 업데이트 실패", it)
                                    _uiState.update { it.copy(errorMessageId = R.string.err_answer_add) }
                                }
                        }
                        .onFailure {
                            Log.e("QuestionViewModel", "퀴즈 정답 제출 실패", it)
                            _uiState.update { it.copy(errorMessageId = R.string.err_answer_add_quiz) }
                        }
                }
            } catch (exception: Exception) {
                Log.e("UserQuestionViewModel", "제출 실패 ", exception)
                _uiState.update { it.copy(errorMessageId = R.string.err_answer_add_quiz) }
            }
        }
    }

    fun exitRealTimeQuiz() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            _uiState.value.currentUserId?.let { currentUserId ->
                quizRepository.waitingRealTimeQuiz(quizId, false, currentUserId)
                    .onSuccess {
                        _uiState.update { it.copy(isLoading = false, isExitSuccess = true) }
                    }
                    .onFailure {
                        _uiState.update { it.copy(isLoading = false, errorMessageId = R.string.err_delete_waiting_user) }
                    }
            }
        }
    }
}
