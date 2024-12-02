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
    val showDialog: Boolean = false,
    val choiceQuestionCreationInfo: ChoiceQuestionCreationInfo = ChoiceQuestionCreationInfo(
        title = "",
        description = "",
        solution = null,
        answer = 0,
        choices = List(4) { "" },
    ),
    val snackBarMessage: String? = null,
    val creationSuccess: Boolean = false,
    val selectedQuestionTypeIndex: Int = 0,
    val items: List<BlankQuestionItem> = listOf(
        BlankQuestionItem.Blank("낱말"),
        BlankQuestionItem.Text("텍스트"),
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
                    _createQuestionUiState.update { it.copy(isLoading = false) }
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
            _createQuestionUiState.update { it.copy(isLoading = false) }
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
        setLoadingState(true)
        viewModelScope.launch {
            aiRepository.getAiQuestion(category).onSuccess {
                val choiceCreationInfo = if (it.choices.size != 4) {
                    ChoiceQuestionCreationInfo(
                        title = it.title,
                        description = it.description,
                        solution = it.solution,
                        answer = getAnswerIndex(it.answer, it.choices),
                        choices = List(4) { "AI가 보기를 찾지 못했습니다." },
                    )
                } else {
                    ChoiceQuestionCreationInfo(
                        title = it.title,
                        description = it.description,
                        solution = it.solution,
                        answer = getAnswerIndex(it.answer, it.choices),
                        choices = it.choices,
                    )
                }
                setAiRecommendedQuestion(
                    choiceCreationInfo,
                )
                _createQuestionUiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                    )
                }
            }.onFailure {
                _createQuestionUiState.update { it.copy(isLoading = false) }
                setNewSnackBarMessage("AI 추천 문제 가져오기에 실패했습니다. 다시 시도해주세요!")
                Log.e("CreateQuestionViewModel", "AI 추천 문제 가져오기 실패")
            }

        }
    }

    private fun setAiRecommendedQuestion(questionCreationInfo: ChoiceQuestionCreationInfo) {
        _createQuestionUiState.update { currentState ->
            currentState.copy(
                choiceQuestionCreationInfo = questionCreationInfo,
            )
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

        viewModelScope.launch {
            questionRepository.createBlankQuestion(
                BlankQuestionCreationInfo(
                    title = _createQuestionUiState.value.choiceQuestionCreationInfo.title,
                    solution = if (_createQuestionUiState.value.choiceQuestionCreationInfo.solution.isNullOrBlank()) null else _createQuestionUiState.value.choiceQuestionCreationInfo.solution,
                    questionContent = blankQuestionList,
                ),
            ).onSuccess { questionId ->
                saveQuestionToQuiz(questionId)
            }.onFailure { exception ->
                Log.e("CreateQuestionViewModel", exception.message, exception)
                setNewSnackBarMessage("문제 생성에 실패했습니다. 다시 시도해주세요!")
            }

        }
    }

}
