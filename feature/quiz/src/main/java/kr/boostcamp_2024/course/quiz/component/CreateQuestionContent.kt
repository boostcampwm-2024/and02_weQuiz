package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizValidateTextField
import kr.boostcamp_2024.course.quiz.R

@Composable
internal fun CreateQuestionContent(
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    title: String,
    description: String? = null,
    solution: String? = null,
    onTitleChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSolutionChanged: (String) -> Unit,
    isBlankQuestion: Boolean,
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        WeQuizValidateTextField(
            modifier = Modifier.focusRequester(focusRequester),
            label = stringResource(id = R.string.txt_question_title_label),
            text = title,
            onTextChanged = onTitleChanged,
            placeholder = stringResource(id = R.string.txt_question_title_placeholder),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            errorMessage = stringResource(id = R.string.txt_question_title_error_message),
            validFun = { it.length <= 50 },
        )
        if (!isBlankQuestion) {
            WeQuizValidateTextField(
                label = stringResource(id = R.string.txt_question_content_label),
                text = description ?: "",
                onTextChanged = onDescriptionChanged,
                placeholder = stringResource(id = R.string.txt_question_content_placeholder),
                minLines = 6,
                maxLines = 6,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                errorMessage = stringResource(id = R.string.txt_question_description_error_message),
                validFun = { it.length <= 100 },
            )
        }
        WeQuizValidateTextField(
            label = stringResource(id = R.string.txt_question_description_label),
            text = solution ?: "",
            onTextChanged = onSolutionChanged,
            placeholder = stringResource(id = R.string.txt_question_description_placeholder),
            minLines = 6,
            maxLines = 6,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            errorMessage = stringResource(id = R.string.txt_question_solution_error_message),
            validFun = { it.length <= 200 },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateQuestionContentPreview() {
    WeQuizTheme{
        CreateQuestionContent(
            focusRequester = FocusRequester(),
            keyboardOptions = KeyboardOptions.Default,
            keyboardActions = KeyboardActions.Default,
            title = "Title",
            description = "Description",
            solution = "Solution",
            onTitleChanged = {},
            onDescriptionChanged = {},
            onSolutionChanged = {},
            isBlankQuestion = false,
        )
    }

}
