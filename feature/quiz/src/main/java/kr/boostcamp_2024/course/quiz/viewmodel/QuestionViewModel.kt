package kr.boostcamp_2024.course.quiz.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.BlankQuestionManager
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.Quiz
import kr.boostcamp_2024.course.domain.model.UserOmrCreationInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.quiz.navigation.QuestionRoute
import javax.inject.Inject

data class QuestionUiState(
    val quiz: BaseQuiz? = null,
    val questions: List<Question> = emptyList(),
    val isSubmitting: Boolean = false,
    val currentPage: Int = 0,
    val selectedIndexList: List<Any> = emptyList(),
    val countDownTime: Int? = null,
    val isLoading: Boolean = false,
    val currentUserId: String? = null,
    val userOmrId: String? = null,
    val blankQuestionContents: List<Map<String, Any>?> = emptyList(),
    val blankWords: List<Map<String, Any>> = emptyList(),
)

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val questionRepository: QuestionRepository,
    private val quizRepository: QuizRepository,
    private val userOmrRepository: UserOmrRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val quizId = savedStateHandle.toRoute<QuestionRoute>().quizId

    private val _uiState: MutableStateFlow<QuestionUiState> = MutableStateFlow(QuestionUiState())
    val uiState: StateFlow<QuestionUiState> = _uiState.asStateFlow()

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

                _uiState.update {
                    it.copy(
                        quiz = quiz,
                        currentUserId = userId,
                    )
                }

                setTimer()
                loadQuestions(quiz.questions)
                updateTimer()
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun setTimer() {
        val currentQuiz = _uiState.value.quiz
        if (currentQuiz is Quiz) {
            _uiState.update { it.copy(countDownTime = currentQuiz.solveTime * 60) }
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
                isLoading = false,
            )
        }
        setNewBlankQuestionManager(0)
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

    fun nextPage() {
        _uiState.update { currentState ->
            val newPage = currentState.currentPage + 1
            setNewBlankQuestionManager(newPage)
            currentState.copy(currentPage = newPage)
        }
    }

    fun previousPage() {
        _uiState.update { currentState ->
            val newPage = currentState.currentPage - 1
            setNewBlankQuestionManager(newPage)
            currentState.copy(currentPage = newPage)
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

    private fun updateTimer() {
        _uiState.value.countDownTime?.let { defaultCountDownTime ->
            viewModelScope.launch {
                var currentCountDownTime: Int = defaultCountDownTime
                while (currentCountDownTime > 0) {
                    delay(1000L)
                    _uiState.update { currentState ->
                        currentState.copy(countDownTime = --currentCountDownTime)
                    }
                }
                submitAnswers()
            }
        }
    }

    fun submitAnswers() {
        viewModelScope.launch {
            try {
                _uiState.value.currentUserId?.let { userId ->
                    _uiState.update { it.copy(isSubmitting = true) }

                    val userOmrCreationInfo = UserOmrCreationInfo(
                        userId = userId,
                        quizId = quizId,
                        answers = _uiState.value.selectedIndexList,
                    )

                    val userOmrId = userOmrRepository.submitQuiz(userOmrCreationInfo)
                    quizRepository.addUserOmrToQuiz(quizId, userOmrId)

                    _uiState.value.questions.forEachIndexed { index, question ->
                        questionRepository.updateCurrentSubmit(
                            uiState.value.currentUserId,
                            question.id,
                            uiState.value.selectedIndexList[index],
                        )
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSubmitting = true,
                            userOmrId = userOmrId,
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
