package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.QuestionCreationInfo
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

data class CreateQuestionUiState(
    val isLoading: Boolean = false,
    val questionCreationInfo: QuestionCreationInfo = QuestionCreationInfo(
        title = "",
        description = "",
        solution = null,
        answer = 0,
        choices = List(4) { "" },
    ),
    val isCreateQuestionValid: Boolean = false,
    val snackBarMessage: String? = null,
    val creationSuccess: Boolean = false,
    val questionKey: String? = null,
)

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : ViewModel() {
    private val _createQuestionUiState: MutableStateFlow<CreateQuestionUiState> = MutableStateFlow(
        CreateQuestionUiState(),
    )
    val createQuestionUiState: StateFlow<CreateQuestionUiState> = _createQuestionUiState

    fun onTitleChanged(title: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                questionCreationInfo = currentState.questionCreationInfo.copy(
                    title = title,
                ),
            )
        }
        checkCreateQuestionValid()
    }

    fun onDescriptionChanged(description: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                questionCreationInfo = currentState.questionCreationInfo.copy(
                    description = description,
                ),
            )
        }
        checkCreateQuestionValid()
    }

    fun onSolutionChanged(solution: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                questionCreationInfo = currentState.questionCreationInfo.copy(
                    solution = solution,
                ),
            )
        }
        checkCreateQuestionValid()
    }

    fun onChoiceTextChanged(changedIndex: Int, changedText: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                questionCreationInfo = currentState.questionCreationInfo.copy(
                    choices = currentState.questionCreationInfo.choices.mapIndexed { index, text ->
                        if (index == changedIndex) changedText else text
                    },
                ),
            )
        }
        checkCreateQuestionValid()
    }

    fun onSelectedChoiceNumChanged(changedNum: Int) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                questionCreationInfo = currentState.questionCreationInfo.copy(
                    answer = changedNum,
                ),
            )
        }
        checkCreateQuestionValid()
    }

    private fun checkCreateQuestionValid() {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                isCreateQuestionValid = currentState.questionCreationInfo.title.isNotBlank() &&
                    currentState.questionCreationInfo.choices.all { it.isNotBlank() },
            )
        }
    }

    fun createQuestion() {
        setLoadingState(true)
        viewModelScope.launch {
            questionRepository.createQuestion(createQuestionUiState.value.questionCreationInfo)
                .onSuccess { questionKey ->
                    _createQuestionUiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            creationSuccess = true,
                            questionKey = questionKey,
                        )
                    }
                }.onFailure { exception ->
                    Log.e("CreateQuestionViewModel", exception.message, exception)
                    setNewSnackBarMessage("문제 생성에 실패했습니다. 다시 시도해주세요!")
                }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                isLoading = isLoading,
            )
        }
    }

    fun setNewSnackBarMessage(message: String?) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                snackBarMessage = message,
            )
        }
    }
}