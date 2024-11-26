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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.QuizResult
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.quiz.navigation.QuizResultRoute
import javax.inject.Inject

data class QuizResultUiState(
    val questions: List<Question>? = null,
    val quizTitle: String? = null,
    val quizResult: QuizResult? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isManager: Boolean = false,
)

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val userOmrRepository: UserOmrRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val userOmrId: String? = savedStateHandle.toRoute<QuizResultRoute>().userOmrId
    private val quizId: String? = savedStateHandle.toRoute<QuizResultRoute>().quizId

    private val userOmrAnswers = MutableStateFlow<List<Any>>(emptyList()) // 사용자 답지
    private val questions = MutableStateFlow<List<Question>>(emptyList()) // 퀴즈 리스트
    private val _uiState: MutableStateFlow<QuizResultUiState> = MutableStateFlow(QuizResultUiState())

    val uiState: StateFlow<QuizResultUiState> = combine(userOmrAnswers, questions, _uiState) { userOmrAnswers, questions, uiState ->
        if (quizId != null) {
            uiState
        } else {
            if (userOmrAnswers.size == questions.size && questions.isNotEmpty()) {
                try {
                    val quizResult = QuizResult(userOmrAnswers = userOmrAnswers, choiceQuestions = questions)
                    uiState.copy(quizResult = quizResult)
                } catch (exception: Exception) {
                    Log.e("QuizResultViewModel", "Failed to create QuizResult", exception)
                    uiState.copy(errorMessage = "퀴즈 결과 생성에 실패했습니다.")
                }
            } else {
                uiState
            }
        }
    }.onStart {
        initViewModel()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        QuizResultUiState(),
    )

    fun initViewModel() {
        if (userOmrId != null) {
            loadUserOmr()
        } else if (quizId != null) {
            loadQuestions(quizId)
            _uiState.update { it.copy(isManager = true) }
        }
    }

    private fun loadQuestions(quizId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            quizRepository.getQuiz(quizId)
                .onSuccess { quiz ->
                    _uiState.update { it.copy(quizTitle = quiz.title) }

                    questionRepository.getQuestions(quiz.questions)
                        .onSuccess { question ->
                            _uiState.update { it.copy(isLoading = false, questions = question) }
                            questions.value = question
                        }
                        .onFailure {
                            Log.e("QuizResultViewModel", "Failed to load questions", it)
                            _uiState.update { it.copy(isLoading = false, errorMessage = "문제 로드에 실패했습니다.") }
                        }
                }
                .onFailure {
                    Log.e("QuizResultViewModel", "Failed to load quiz", it)
                    _uiState.update { it.copy(isLoading = false, errorMessage = "퀴즈 로드에 실패했습니다.") }
                }
        }
    }

    private fun loadUserOmr() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            if (userOmrId != null) {
                userOmrRepository.getUserOmr(userOmrId)
                    .onSuccess { userOmr ->
                        _uiState.update { it.copy(isLoading = false) }
                        userOmrAnswers.value = userOmr.answers

                        loadQuestions(userOmr.quizId)

                    }
                    .onFailure {
                        Log.e("QuizResultViewModel", "Failed to load userOmr", it)
                        _uiState.update { it.copy(isLoading = false, errorMessage = "답지 로드에 실패했습니다.") }
                    }
            }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
