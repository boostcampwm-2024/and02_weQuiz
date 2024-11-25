package kr.boostcamp_2024.course.quiz.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.ChoiceQuestionCreationInfo
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.quiz.navigation.CreateQuestionRoute
import javax.inject.Inject

data class CreateQuestionUiState(
    val isLoading: Boolean = false,
    val choiceQuestionCreationInfo: ChoiceQuestionCreationInfo = ChoiceQuestionCreationInfo(
        title = "",
        description = "",
        solution = null,
        answer = 0,
        choices = List(4) { "" },
    ),
    val isCreateQuestionValid: Boolean = false,
    val snackBarMessage: String? = null,
    val creationSuccess: Boolean = false,
)

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val quizRepository: QuizRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val quizId: String = savedStateHandle.toRoute<CreateQuestionRoute>().quizId

    private val _createQuestionUiState: MutableStateFlow<CreateQuestionUiState> = MutableStateFlow(CreateQuestionUiState())
    val createQuestionUiState: StateFlow<CreateQuestionUiState> = _createQuestionUiState.asStateFlow()

    fun onTitleChanged(title: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                choiceQuestionCreationInfo = currentState.choiceQuestionCreationInfo.copy(
                    title = title,
                ),
            )
        }
        checkCreateQuestionValid()
    }

    fun onDescriptionChanged(description: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                choiceQuestionCreationInfo = currentState.choiceQuestionCreationInfo.copy(
                    description = description,
                ),
            )
        }
        checkCreateQuestionValid()
    }

    fun onSolutionChanged(solution: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                choiceQuestionCreationInfo = currentState.choiceQuestionCreationInfo.copy(
                    solution = solution,
                ),
            )
        }
        checkCreateQuestionValid()
    }

    fun onChoiceTextChanged(changedIndex: Int, changedText: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                choiceQuestionCreationInfo = currentState.choiceQuestionCreationInfo.copy(
                    choices = currentState.choiceQuestionCreationInfo.choices.mapIndexed { index, text ->
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
                choiceQuestionCreationInfo = currentState.choiceQuestionCreationInfo.copy(answer = changedNum),
            )
        }
        checkCreateQuestionValid()
    }

    private fun checkCreateQuestionValid() {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                isCreateQuestionValid = currentState.choiceQuestionCreationInfo.title.isNotBlank() &&
                    currentState.choiceQuestionCreationInfo.description.isNotBlank() &&
                    currentState.choiceQuestionCreationInfo.choices.all { it.isNotBlank() },
            )
        }
    }

    fun createQuestion() {
        setLoadingState(true)
        viewModelScope.launch {
            val currentQuestionCreationInfo = createQuestionUiState.value.choiceQuestionCreationInfo
            val questionCreationInfo = currentQuestionCreationInfo.copy(
                solution = if (currentQuestionCreationInfo.solution.isNullOrBlank()) null else currentQuestionCreationInfo.solution,
            )
            questionRepository.createQuestion(questionCreationInfo)
                .onSuccess { questionId ->
                    saveQuestionToQuiz(questionId)
                }.onFailure { exception ->
                    Log.e("CreateQuestionViewModel", exception.message, exception)
                    setNewSnackBarMessage("문제 생성에 실패했습니다. 다시 시도해주세요!")
                }
        }
    }

    private suspend fun saveQuestionToQuiz(questionId: String) {
        try {
            quizRepository.addQuestionToQuiz(quizId, questionId).getOrThrow()

            _createQuestionUiState.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    creationSuccess = true,
                )
            }
        } catch (exception: Exception) {
            // todo: 문제를 삭제해야 하지 않을까?
            Log.e("CreateQuestionViewModel", exception.message, exception)
            setNewSnackBarMessage("문제 저장에 실패했습니다. 다시 시도해주세요!")
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
