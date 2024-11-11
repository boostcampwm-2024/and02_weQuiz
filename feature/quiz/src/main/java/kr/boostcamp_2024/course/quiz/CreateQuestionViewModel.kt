package kr.boostcamp_2024.course.quiz

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kr.boostcamp_2024.course.domain.model.QuestionRequestVO
import javax.inject.Inject

@HiltViewModel
class CreateQuestionViewModel @Inject constructor() : ViewModel() {
	private val _createQuestionState: MutableStateFlow<QuestionRequestVO> = MutableStateFlow(
		QuestionRequestVO(
			title = "",
			description = "",
			solution = null,
			answer = 0,
			choices = listOf("", "", "", ""),
		),
	)

	val createQuestionState: StateFlow<QuestionRequestVO> = _createQuestionState
	private val _isCreateQuestionValid: MutableStateFlow<Boolean> = MutableStateFlow(false)
	val isCreateQuestionValid: StateFlow<Boolean> = _isCreateQuestionValid

	fun onTitleChanged(title: String) {
		_createQuestionState.value = createQuestionState.value.copy(
			title = title,
		)
		checkCreateQuestionValid()
	}

	fun onDescriptionChanged(description: String) {
		_createQuestionState.value = createQuestionState.value.copy(
			description = description,
		)
		checkCreateQuestionValid()
	}

	fun onSolutionChanged(solution: String) {
		_createQuestionState.value = createQuestionState.value.copy(
			solution = solution,
		)
		checkCreateQuestionValid()
	}

	fun onChoiceTextChanged(changedIndex: Int, changedText: String) {
		_createQuestionState.value = createQuestionState.value.copy(
			choices = createQuestionState.value.choices.mapIndexed { index, text ->
				if (index == changedIndex) changedText else text
			},
		)
		checkCreateQuestionValid()
	}

	fun onSelectedChoiceNumChanged(changedNum: Int) {
		_createQuestionState.value = createQuestionState.value.copy(
			answer = changedNum,
		)
		checkCreateQuestionValid()
	}

	private fun checkCreateQuestionValid() {
		val curCreateQuestionState = createQuestionState.value
		_isCreateQuestionValid.value =
			curCreateQuestionState.title.isNotBlank() && !curCreateQuestionState.solution.isNullOrBlank() && curCreateQuestionState.choices.all { it.isNotBlank() }
	}
}
