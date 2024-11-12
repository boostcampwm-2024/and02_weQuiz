package kr.boostcamp_2024.course.quiz

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kr.boostcamp_2024.course.domain.model.QuestionCreationInfo
import javax.inject.Inject

data class CreateQuestionUiState(
    val questionCreationInfo: QuestionCreationInfo = QuestionCreationInfo(
        title = "",
        description = "",
        solution = null,
        answer = 0,
        choices = List(4) { "" },
    ),
    val isCreateQuestionValid: Boolean = false,
)

@HiltViewModel
class CreateQuestionViewModel @Inject constructor() : ViewModel() {
    private val _createQuestionUiState: MutableStateFlow<CreateQuestionUiState> = MutableStateFlow(
        CreateQuestionUiState()
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
                    answer = changedNum
                )
            )
        }
        checkCreateQuestionValid()
    }

    private fun checkCreateQuestionValid() {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                isCreateQuestionValid = currentState.questionCreationInfo.title.isNotBlank() &&
                        currentState.questionCreationInfo.choices.all { it.isNotBlank() }
            )
        }
    }
}
