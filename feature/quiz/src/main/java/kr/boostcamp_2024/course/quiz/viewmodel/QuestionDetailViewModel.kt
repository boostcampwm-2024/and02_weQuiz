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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.BlankQuestion
import kr.boostcamp_2024.course.domain.model.ChoiceQuestion
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.quiz.navigation.QuestionDetailRoute
import javax.inject.Inject

data class DetailUiState(
    val question: Question? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userAnswer: List<Int> = listOf(0, 0, 0, 0),
    // TODO 출제자 이미지 추가
)

@HiltViewModel
class QuestionDetailViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val questionId: String = savedStateHandle.toRoute<QuestionDetailRoute>().questionId

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState
        .onStart {
            loadQuestionDetail()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000L), DetailUiState())

    private fun loadQuestionDetail() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            questionRepository.getQuestion(questionId).onSuccess { question ->
                _uiState.update {
                    when(question){
                        is ChoiceQuestion -> {
                            it.copy(
                                isLoading = false,
                                question = question,
                                userAnswer = question.userAnswers,
                            )
                        }
                        is BlankQuestion -> {
                            it.copy(
                                isLoading = false,
                                question = question,
                            )
                        }
                    }
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "문제 로딩에 실패하였습니다.",
                    )
                }
                Log.e("DetailViewModel", "loadQuestionDetail: $throwable")
            }

        }
    }

    fun shownErrorMessage() {
        _uiState.update {
            it.copy(errorMessage = null)
        }

    }
}
