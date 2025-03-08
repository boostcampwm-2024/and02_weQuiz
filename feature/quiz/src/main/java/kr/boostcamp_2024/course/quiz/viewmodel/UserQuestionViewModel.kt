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
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.BlankQuestionManager
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.domain.model.UserOmrCreationInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.quiz.navigation.QuestionRoute
import javax.inject.Inject

data class UserQuestionUiState(
    val quiz: BaseQuiz? = null,
    val questions: List<Question> = emptyList(),
    val ownerName: String? = null,
    val currentPage: Int = 0,
    val selectedIndexList: List<Any?> = emptyList(),
    val submittedIndexList: List<Any?> = emptyList(),
    val isLoading: Boolean = false,
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
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val quizId = savedStateHandle.toRoute<QuestionRoute>().quizId

    private val _uiState: MutableStateFlow<UserQuestionUiState> = MutableStateFlow(UserQuestionUiState())
    val uiState: StateFlow<UserQuestionUiState> = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    val blankQuestionManager = BlankQuestionManager(::setNewBlankContents)

    init {
        initial()
    }

    private fun initial() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val userId = authRepository.getUserKey()
                val quiz = quizRepository.getQuiz(quizId)

                loadQuestions(quiz.questions)
                if (quiz is RealTimeQuiz) {
                    getQuizOwnerName(quiz.ownerId)
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        currentUserId = userId,
                        quiz = quiz,
                    )
                }

            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun getQuizOwnerName(ownerId: String) {
        val user = userRepository.getUser(ownerId)
        _uiState.update { currentState ->
            currentState.copy(
                ownerName = user.name,
            )
        }
    }

    private suspend fun loadQuestions(questionIds: List<String>) {
        val questions = questionRepository.getQuestions(questionIds)
        val baseSelectedList = questions.map {
            if (it is BlankQuestion) {
                mapOf<Int, String?>()
            } else {
                4
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
        loadRealTimeQuestions(questionIds)
    }

    private suspend fun loadRealTimeQuestions(questionIds: List<String>) {
        val questionList = questionRepository.getRealTimeQuestions(questionIds)

        questionList.forEachIndexed { index, questionFlow ->
            viewModelScope.launch {
                questionFlow.catch {
                    _errorFlow.emit(it)
                }.collect { question ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            questions = currentState.questions.toMutableList().apply {
                                this[index] = question
                            },
                        )
                    }
                }
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
            quizRepository.observeRealTimeQuiz(quizId).collect { quiz ->
                _uiState.update {
                    setNewBlankQuestionManager(quiz.currentQuestion)
                    it.copy(
                        currentPage = quiz.currentQuestion,
                        isSubmitted = false,
                        isQuizFinished = quiz.isFinished,
                    )
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
            try {
                _uiState.update { it.copy(isLoading = true) }

                questionRepository.updateCurrentSubmit(
                    currentUser,
                    questionId,
                    _uiState.value.selectedIndexList[_uiState.value.currentPage],
                )
                val updatedList = _uiState.value.submittedIndexList.toMutableList().apply {
                    this[_uiState.value.currentPage] = _uiState.value.selectedIndexList[_uiState.value.currentPage]
                }

                _uiState.update {
                    it.copy(
                        isSubmitted = true,
                        submittedIndexList = updatedList,
                    )
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun submitAnswers() {
        viewModelScope.launch {
            try {
                _uiState.value.currentUserId?.let { userId ->
                    _uiState.update { it.copy(isLoading = true) }

                    val userOmrCreationInfo = UserOmrCreationInfo(
                        userId = userId,
                        quizId = quizId,
                        answers = _uiState.value.submittedIndexList as List<Any>,
                    )

                    val userOmrId = userOmrRepository.submitQuiz(userOmrCreationInfo)
                    quizRepository.addUserOmrToQuiz(quizId, userOmrId)

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            userOmrId = userOmrId,
                        )
                    }
                }
            } catch (exception: Exception) {
                _errorFlow.emit(exception)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun exitRealTimeQuiz() {
        viewModelScope.launch {
            try {
                _uiState.value.currentUserId?.let { currentUserId ->
                    _uiState.update { it.copy(isLoading = true) }
                    quizRepository.waitingRealTimeQuiz(quizId, false, currentUserId)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isExitSuccess = true,
                        )
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
