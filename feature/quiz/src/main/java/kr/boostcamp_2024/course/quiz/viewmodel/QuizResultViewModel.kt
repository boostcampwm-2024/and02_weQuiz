package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
import javax.inject.Inject

data class QuizResultUiState(
    val quizTitle: String? = null,
    val quizResult: QuizResult? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val userOmrRepository: UserOmrRepository,
) : ViewModel() {
    private val userOmrId = "4PxcQeA4mGtEiUXoffn4"

    private val userOmrAnswers = MutableStateFlow<List<Int>>(emptyList()) // 사용자 답지
    private val questions = MutableStateFlow<List<Question>>(emptyList()) // 퀴즈 리스트
    private val _uiState = MutableStateFlow(QuizResultUiState())

    val uiState = combine(userOmrAnswers, questions, _uiState) { userOmrAnswers, questions, uiState ->
        val quizResult =
            when (userOmrAnswers.size == questions.size && questions.isNotEmpty()) {
                true -> QuizResult(userOmrAnswers = userOmrAnswers, questions = questions)
                false -> null
            }

        uiState.copy(quizResult = quizResult)
    }.onStart {
        loadUserOmr()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuizResultUiState())

    private fun loadQuestions(quizId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            quizRepository.getQuiz(quizId)
                .onSuccess { quiz ->
                    _uiState.update { it.copy(quizTitle = quiz.title) }

                    questionRepository.getQuestions(quiz.questions)
                        .onSuccess {
                            _uiState.update { it.copy(isLoading = false) }
                            questions.value = it
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

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}