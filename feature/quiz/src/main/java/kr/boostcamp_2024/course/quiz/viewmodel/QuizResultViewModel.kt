package kr.boostcamp_2024.course.quiz.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val isLoading: Boolean = false,
    val isManager: Boolean = false,
    val userOmrAnswers: List<Any> = emptyList(),
) {
    val quizResult: QuizResult? =
        if (questions?.isNotEmpty() == true && userOmrAnswers.size == questions.size) {
            QuizResult(userOmrAnswers = userOmrAnswers, questions = questions)
        } else {
            null
        }
}

@HiltViewModel
class QuizResultViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val userOmrRepository: UserOmrRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val userOmrId: String? = savedStateHandle.toRoute<QuizResultRoute>().userOmrId
    private val quizId: String? = savedStateHandle.toRoute<QuizResultRoute>().quizId

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    private val _uiState: MutableStateFlow<QuizResultUiState> = MutableStateFlow(QuizResultUiState())

    val uiState: StateFlow<QuizResultUiState> = _uiState.asStateFlow()
        .onStart {
            initViewModel()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            QuizResultUiState(),
        )

    private fun initViewModel() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                if (userOmrId != null) {
                    loadUserOmr(userOmrId)
                } else if (quizId != null) {
                    loadQuestions(quizId)
                    _uiState.update { it.copy(isManager = true) }
                }

            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun loadQuestions(quizId: String) {
        val quiz = quizRepository.getQuiz(quizId)
        val question = questionRepository.getQuestions(quiz.questions)

        _uiState.update {
            it.copy(
                isLoading = false,
                questions = question,
                quizTitle = quiz.title,
            )
        }
    }

    private suspend fun loadUserOmr(userOmrId: String) {
        val userOmr = userOmrRepository.getUserOmr(userOmrId)

        _uiState.update {
            it.copy(
                userOmrAnswers = userOmr.answers,
            )
        }

        loadQuestions(userOmr.quizId)
    }
}
