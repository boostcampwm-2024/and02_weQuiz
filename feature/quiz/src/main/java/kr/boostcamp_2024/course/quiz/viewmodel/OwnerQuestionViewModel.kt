package kr.boostcamp_2024.course.quiz.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.BlankQuestionManager
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import javax.inject.Inject

data class RealTimeWithOwnerQuestionUiState(
    val quiz: RealTimeQuiz? = null,
    val questions: List<Question?> = emptyList(),
    val ownerName: String? = null,
    val currentPage: Int = 0,
    val isLoading: Boolean = false,
    val currentUserId: String? = null,
    val isQuizFinished: Boolean = false,
    val blankQuestionContents: List<Map<String, Any>?> = emptyList(),
    val blankWords: List<Map<String, Any>> = emptyList(),
)

@HiltViewModel
class OwnerQuestionViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<RealTimeWithOwnerQuestionUiState> = MutableStateFlow(RealTimeWithOwnerQuestionUiState())
    val uiState: StateFlow<RealTimeWithOwnerQuestionUiState> = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    val blankQuestionManager = BlankQuestionManager(::setNewBlankContents)

    fun initQuizData(quiz: RealTimeQuiz?, currentUserId: String?) {
        viewModelScope.launch {
            try {
                quiz?.let {
                    setLoadingState(true)

                    val user = userRepository.getUser(quiz.ownerId)
                    val questionList = questionRepository.getRealTimeQuestions(quiz.questions)

                    _uiState.update {
                        it.copy(
                            quiz = quiz,
                            ownerName = user.name,
                            currentUserId = currentUserId,
                            questions = List(quiz.questions.size) { null },
                            isLoading = false,
                        )
                    }

                    questionList.forEachIndexed { index, questionFlow ->
                        questionFlow.collect {
                            _uiState.update { currentState ->
                                currentState.copy(
                                    questions = currentState.questions.toMutableList().apply {
                                        this[index] = it
                                    },
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoadingState(false)
            }
        }
    }

    fun setQuizFinished() {
        viewModelScope.launch {
            try {
                setLoadingState(true)

                val currentQuizId = requireNotNull(_uiState.value.quiz?.id)
                quizRepository.setQuizFinished(currentQuizId)

                _uiState.update {
                    it.copy(
                        isQuizFinished = true,
                        isLoading = false,
                    )
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoadingState(false)
            }
        }
    }

    fun nextPage() {
        updateCurrentPage(_uiState.value.currentPage + 1)
    }

    fun previousPage() {
        updateCurrentPage(_uiState.value.currentPage - 1)
    }

    private fun updateCurrentPage(pageIdx: Int) {
        viewModelScope.launch {
            try {
                val currentQuizId = requireNotNull(_uiState.value.quiz?.id)

                quizRepository.updateQuizCurrentQuestion(currentQuizId, pageIdx)

                _uiState.update {
                    it.copy(currentPage = pageIdx)
                }
                setNewBlankQuestionManager(pageIdx)
            } catch (e: Exception) {
                _errorFlow.emit(e)
            }
        }
    }

    private fun setNewBlankQuestionManager(pageIdx: Int) {
        val currentQuestion = _uiState.value.questions[pageIdx]
        if (currentQuestion is BlankQuestion) {
            blankQuestionManager.setNewQuestions(
                isOwner = true,
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

    private fun setLoadingState(isLoading: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isLoading = isLoading)
        }
    }
}
