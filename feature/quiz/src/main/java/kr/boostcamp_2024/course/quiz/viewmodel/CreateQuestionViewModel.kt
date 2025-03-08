package kr.boostcamp_2024.course.quiz.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.BlankQuestionCreationInfo
import kr.boostcamp_2024.course.domain.model.ChoiceQuestionCreationInfo
import kr.boostcamp_2024.course.domain.repository.AiRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.quiz.navigation.CreateQuestionRoute
import javax.inject.Inject

sealed class BlankQuestionItem {
    data class Blank(
        val text: String,
    ) : BlankQuestionItem()

    data class Text(
        val text: String,
    ) : BlankQuestionItem()
}

data class CreateQuestionUiState(
    val isLoading: Boolean = false,
    val isAiLoading: Boolean = false,
    val showDialog: Boolean = false,
    val choiceQuestionCreationInfo: ChoiceQuestionCreationInfo = ChoiceQuestionCreationInfo(
        title = "",
        description = "",
        solution = null,
        answer = 0,
        choices = List(4) { "" },
    ),
    val creationSuccess: Boolean = false,
    val selectedQuestionTypeIndex: Int = 0,
    val items: List<BlankQuestionItem> = listOf(
        BlankQuestionItem.Blank("blank"),
        BlankQuestionItem.Text("text"),
    ),
) {
    val isCreateQuestionValid: Boolean = choiceQuestionCreationInfo.title.length in 1..50 &&
        choiceQuestionCreationInfo.description.length in 1..100 &&
        choiceQuestionCreationInfo.solution?.length in 0..200 &&
        choiceQuestionCreationInfo.choices.all {
            it.isNotBlank()
        } &&
        choiceQuestionCreationInfo.answer in (0..3)

    val isCreateBlankQuestionValid: Boolean = items.any { it is BlankQuestionItem.Blank } &&
        items.all {
            (it is BlankQuestionItem.Text && it.text.isNotBlank()) ||
                (it is BlankQuestionItem.Blank && it.text.isNotBlank())
        } &&
        choiceQuestionCreationInfo.title.length in 1..50 &&
        choiceQuestionCreationInfo.solution?.length in 0..200

    val isCreateBlankButtonValid: Boolean = items.count { it is BlankQuestionItem.Blank } < 5

    val isCreateTextButtonValid: Boolean = items.count { it is BlankQuestionItem.Text } < 5

    val isBlankQuestion: Boolean = selectedQuestionTypeIndex == 1
}

@HiltViewModel
class CreateQuestionViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val quizRepository: QuizRepository,
    private val aiRepository: AiRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val quizId: String = savedStateHandle.toRoute<CreateQuestionRoute>().quizId

    private val _createQuestionUiState: MutableStateFlow<CreateQuestionUiState> = MutableStateFlow(CreateQuestionUiState())
    val createQuestionUiState: StateFlow<CreateQuestionUiState> = _createQuestionUiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    fun onTitleChanged(title: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                choiceQuestionCreationInfo = currentState.choiceQuestionCreationInfo.copy(
                    title = title,
                ),
            )
        }
    }

    fun onDescriptionChanged(description: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                choiceQuestionCreationInfo = currentState.choiceQuestionCreationInfo.copy(
                    description = description,
                ),
            )
        }
    }

    fun onSolutionChanged(solution: String) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                choiceQuestionCreationInfo = currentState.choiceQuestionCreationInfo.copy(
                    solution = solution,
                ),
            )
        }
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
    }

    fun onSelectedChoiceNumChanged(changedNum: Int) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                choiceQuestionCreationInfo = currentState.choiceQuestionCreationInfo.copy(answer = changedNum),
            )
        }
    }

    fun createQuestion() {
        viewModelScope.launch {
            try {
                setLoadingState(true)

                val currentQuestionCreationInfo = createQuestionUiState.value.choiceQuestionCreationInfo
                val questionCreationInfo = currentQuestionCreationInfo.copy(
                    solution = if (currentQuestionCreationInfo.solution.isNullOrBlank()) null else currentQuestionCreationInfo.solution,
                )

                val questionId = questionRepository.createQuestion(questionCreationInfo)
                quizRepository.addQuestionToQuiz(quizId, questionId)

                _createQuestionUiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        creationSuccess = true,
                    )
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoadingState(false)
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

    fun showDialog() {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                showDialog = true,
            )
        }
    }

    fun closeDialog() {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                showDialog = false,
            )
        }
    }

    fun getAiRecommendedQuestion(category: String) {
        viewModelScope.launch {
            try {
                _createQuestionUiState.update { it.copy(isAiLoading = true) }

                val aiQuestion = aiRepository.getAiQuestion(category)
                val choiceCreationInfo = if (aiQuestion.choices.size != 4) {
                    ChoiceQuestionCreationInfo(
                        title = aiQuestion.title,
                        description = aiQuestion.description,
                        solution = aiQuestion.solution,
                        answer = getAnswerIndex(aiQuestion.answer, aiQuestion.choices),
                        choices = List(4) { "AI could not generate choices." },
                    )
                } else {
                    ChoiceQuestionCreationInfo(
                        title = aiQuestion.title,
                        description = aiQuestion.description,
                        solution = aiQuestion.solution,
                        answer = getAnswerIndex(aiQuestion.answer, aiQuestion.choices),
                        choices = aiQuestion.choices,
                    )
                }
                _createQuestionUiState.update { currentState ->
                    currentState.copy(
                        isAiLoading = false,
                        choiceQuestionCreationInfo = choiceCreationInfo,
                    )
                }
            } catch (e: Exception) {
                _createQuestionUiState.update { it.copy(isAiLoading = false) }
                _errorFlow.emit(e)
                setLoadingState(false)
            }
        }
    }

    private fun getAnswerIndex(answer: String, choices: List<String>): Int = choices.indexOf(answer)

    fun onQuestionTypeIndexChange(index: Int) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                selectedQuestionTypeIndex = index,
            )
        }
    }

    fun addBlankItem() {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                items = currentState.items + BlankQuestionItem.Blank(""),
            )
        }
    }

    fun addTextItem() {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                items = currentState.items + BlankQuestionItem.Text(""),
            )
        }
    }

    fun onBlankQuestionItemValueChanged(word: String, index: Int) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                items = currentState.items.mapIndexed { i, item ->
                    if (item is BlankQuestionItem.Blank && i == index) {
                        BlankQuestionItem.Blank(word)
                    } else if (item is BlankQuestionItem.Text && i == index) {
                        BlankQuestionItem.Text(word)
                    } else {
                        item
                    }
                },
            )
        }
    }

    fun onContentRemove(index: Int) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                items = currentState.items.filterIndexed { i, _ -> i != index },
            )
        }

    }

    fun onCreateBlankQuestion() {
        viewModelScope.launch {
            try {
                setLoadingState(true)

                val blankQuestionList = mutableListOf<LinkedHashMap<String, String>>()
                _createQuestionUiState.value.items.forEachIndexed { _, item ->
                    val blankQuestionMap = LinkedHashMap<String, String>()
                    if (item is BlankQuestionItem.Blank) {
                        blankQuestionMap["text"] = item.text
                        blankQuestionMap["type"] = "blank"
                    } else if (item is BlankQuestionItem.Text) {
                        blankQuestionMap["text"] = item.text
                        blankQuestionMap["type"] = "text"
                    }
                    blankQuestionList.add(blankQuestionMap)
                }

                val questionId = questionRepository.createBlankQuestion(
                    BlankQuestionCreationInfo(
                        title = _createQuestionUiState.value.choiceQuestionCreationInfo.title,
                        solution = if (_createQuestionUiState.value.choiceQuestionCreationInfo.solution.isNullOrBlank()) null else _createQuestionUiState.value.choiceQuestionCreationInfo.solution,
                        questionContent = blankQuestionList,
                    ),
                )
                quizRepository.addQuestionToQuiz(quizId, questionId)

                _createQuestionUiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        creationSuccess = true,
                    )
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                setLoadingState(false)
            }
        }
    }

}
