package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

data class DetailUiState(
    val title: String = "",
    val description: String = "",
    val solution: String? = "",
    val answer: Int = 1,
    val choices: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    // TODO 출제자 이미지 추가
)

@HiltViewModel
class QuestionDetailViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadQuestionDetail()
    }

    private fun loadQuestionDetail() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }
            questionRepository.getQuestion("YkQXipSR1RGJDfwtcOzS").onSuccess { question ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        title = question.title,
                        description = question.description,
                        solution = question.solution,
                        answer = question.answer,
                        choices = question.choices,
                    )
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
