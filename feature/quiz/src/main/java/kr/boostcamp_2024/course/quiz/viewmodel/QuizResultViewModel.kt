package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.QuizResult
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import javax.inject.Inject

data class QuizResultUiState(
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
    // TestQuizId
    private val userOmrId = "4PxcQeA4mGtEiUXoffn4"

    private val userOmrAnswers = MutableStateFlow<List<Int>>(emptyList()) // 사용자 답지
    private val questions = MutableStateFlow<List<Question>>(emptyList()) // 퀴즈 리스트
    private val isLoading = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    val uiState = combine(userOmrAnswers, questions, isLoading, errorMessage) { userOmrAnswers, questions, isLoading, errorMessage ->
        val quizResult =
            when (userOmrAnswers.size == questions.size && questions.isNotEmpty()) {
                true -> QuizResult(userOmrAnswers = userOmrAnswers, questions = questions)
                false -> null
            }

        QuizResultUiState(
            quizResult = quizResult,
            isLoading = isLoading,
            errorMessage = errorMessage,
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), QuizResultUiState())

    init {
        loadUserOmer()
    }

    private fun loadQuestions(quizId: String) {
        viewModelScope.launch {
            isLoading.value = true

            quizRepository.getQuiz(quizId)
                .onSuccess { quiz ->
                    questionRepository.getQuestions(quiz.questions)
                        .onSuccess {
                            isLoading.value = false
                            questions.value = it
                        }
                        .onFailure {
                            Log.e("QuizResultViewModel", "Failed to load questions", it)
                            isLoading.value = false
                            errorMessage.value = "문제 로드에 실패했습니다."
                        }
                }
                .onFailure {
                    Log.e("QuizResultViewModel", "Failed to load quiz", it)
                    isLoading.value = false
                    errorMessage.value = "퀴즈 로드에 실패했습니다."
                }
        }
    }

    private fun loadUserOmer() {
        viewModelScope.launch {
            isLoading.value = true

            userOmrRepository.getUserOmr(userOmrId)
                .onSuccess { userOmr ->
                    isLoading.value = false
                    userOmrAnswers.value = userOmr.answers

                    loadQuestions(userOmr.quizId)
                }
                .onFailure {
                    Log.e("QuizResultViewModel", "Failed to load userOmr", it)
                    isLoading.value = false
                    errorMessage.value = "답지 로드에 실패했습니다."
                }
        }
    }

    fun shownErrorMessage() {
        errorMessage.value = null
    }
}
