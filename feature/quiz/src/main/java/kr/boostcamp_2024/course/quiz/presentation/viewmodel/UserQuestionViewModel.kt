package kr.boostcamp_2024.course.quiz.presentation.viewmodel

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
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.UserOmrCreationInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.quiz.R
import kr.boostcamp_2024.course.quiz.navigation.QuestionRoute
import javax.inject.Inject

/* TODO QUizNavigation에서 바꿔주고 쓰기 */
data class UserQuestionUiState(
    val quiz: BaseQuiz? = null,
    val questions: List<Question> = emptyList(),
    val isSubmitting: Boolean = false,
    val currentPage: Int = 0,
    val selectedIndexList: List<Int> = emptyList(),

    val isLoading: Boolean = false,
    val errorMessageId: Int? = null,
    val currentUserId: String? = null,
    val userOmrId: String? = null,
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

    private val _uiState: MutableStateFlow<UserQuestionUiState> = MutableStateFlow(UserQuestionUiState())
    val uiState: StateFlow<UserQuestionUiState> = _uiState
        .onStart {
            initial()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UserQuestionUiState(),
        )

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
//            updateTimer()
        }
    }

    private fun loadQuestions(questionIds: List<String>) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true)
            }
            questionRepository.getQuestions(questionIds)
                .onSuccess { questions ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            questions = questions,
                            selectedIndexList = List<Int>(questions.size) { -1 },
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


//    fun nextPage() {
//        // TODO: 스냅샷을 추가하자
//        _uiState.update { currentState ->
//            currentState.copy(currentPage = currentState.currentPage + 1)
//        }
//    }

    fun nextPage() {
        viewModelScope.launch {
            quizRepository.observeCurrentPage(quizId)
                .collect { result ->
                    result
                        .onSuccess { newCurrentPage ->
                            newCurrentPage?.let {
                                _uiState.update {
                                    it.copy(currentPage = newCurrentPage) // TODO: 잘 오르나 확인하기
                                }
                            }
                        }.onFailure {
                            Log.e("UserQuestionViewModel", "페이지 로드 실패", it)
                            _uiState.update { it.copy(errorMessageId = R.string.err_load_current_page) }
                        }
                }

        }

    }
//
//    fun previousPage() {
//        _uiState.update { currentState ->
//            currentState.copy(currentPage = currentState.currentPage - 1)
//        }
//    }

//    fun updateTimer() {
//        viewModelScope.launch {
//            while (_uiState.value.countDownTime > 0) {
//                kotlinx.coroutines.delay(1000L)
//                _uiState.update { currentState ->
//                    currentState.copy(countDownTime = currentState.countDownTime - 1)
//                }
//            }
//        }
//    }

    fun submitQuestion(questionId: String) {
        viewModelScope.launch {
            val result = questionRepository.updateCurrentSubmit(questionId)
            result.onSuccess {
                // TODO: 성공작업
            }.onFailure { exception ->
                // TODO: 실패작업
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
                    answers = _uiState.value.selectedIndexList.map { it },
                )
                userOmrRepository.submitQuiz(userOmrCreationInfo)
                    .onSuccess { userOmrId ->
                        quizRepository.addUserOmrToQuiz(quizId, userOmrId)
                            .onSuccess {
                                _uiState.update { it.copy(isSubmitting = true, userOmrId = userOmrId) }
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
