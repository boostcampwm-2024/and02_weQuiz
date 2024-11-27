package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

data class QuestionUiState(
    val quiz: BaseQuiz? = null,
    val questions: List<Question> = emptyList(),
    val isSubmitting: Boolean = false,
    val currentPage: Int = 0,
    val selectedIndexList: List<Any> = emptyList(),
    val countDownTime: Int = 20 * 60,
    val isLoading: Boolean = false,
    val errorMessageId: Int? = null,
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
    val blankQuestionManager = BlankQuestionManager(::setNewBlankContents)

    init {
        initial()
    }

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
                    _uiState.update { it.copy(isLoading = false, errorMessageId = R.string.err_load_quiz) }
                }
            updateTimer()
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
                            isLoading = false,
                        )
                    }
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

    fun showErrorMessage(errorMessageId: Int) {
        _uiState.update { currentState ->
            currentState.copy(errorMessageId = errorMessageId)
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

    fun updateTimer() {
        viewModelScope.launch {
            while (_uiState.value.countDownTime > 0) {
                delay(1000L)
                _uiState.update { currentState ->
                    currentState.copy(countDownTime = currentState.countDownTime - 1)
                }
            }
        }
    }

    fun submitAnswers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }

            _uiState.value.currentUserId?.let { userId ->
                val userOmrCreationInfo = UserOmrCreationInfo(
                    userId = userId,
                    quizId = quizId,
                    answers = _uiState.value.selectedIndexList,
                )
                userOmrRepository.submitQuiz(userOmrCreationInfo)
                    .onSuccess { userOmrId ->
                        quizRepository.addUserOmrToQuiz(quizId, userOmrId)
                            .onSuccess {
                                _uiState.update { it.copy(isSubmitting = true, userOmrId = userOmrId) }
                                _uiState.value.questions.forEachIndexed { index, question ->
                                    questionRepository.updateCurrentSubmit(
                                        uiState.value.currentUserId,
                                        question.id,
                                        uiState.value.selectedIndexList[index],
                                    )
                                        .onSuccess {
                                            _uiState.update { it.copy(isSubmitting = true) }
                                        }
                                        .onFailure {
                                            Log.e("QuestionViewModel", "user_answers 업데이트 실패")
                                            _uiState.update { it.copy(isSubmitting = false, errorMessageId = R.string.err_add_user_answers) }
                                        }
                                }
                            }
                            .onFailure {
                                Log.e("QuestionViewModel", "userOmrId 업데이트 실패", it)
                                _uiState.update { it.copy(isSubmitting = false, errorMessageId = R.string.err_answer_add) }
                            }
                    }
                    .onFailure {
                        Log.e("QuestionViewModel", "퀴즈 정답 제출 실패", it)
                        _uiState.update { it.copy(isSubmitting = false, errorMessageId = R.string.err_answer_add_quiz) }
                    }
            }
        }
    }
}
