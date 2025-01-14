package kr.boostcamp_2024.course.quiz.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.quiz.R

@Composable
internal fun QuizAiDialog(
    onDismissButtonClick: () -> Unit,
    onConfirmButtonClick: (String) -> Unit,
) {
    var category by remember { mutableStateOf("") }
    WeQuizBaseDialog(
        title = stringResource(R.string.txt_create_question_ai_title),
        dialogImage = painterResource(id = R.drawable.quiz_create_ai_profile),
        confirmTitle = stringResource(R.string.txt_create_question_ai_confirmTitle),
        dismissTitle = stringResource(R.string.txt_create_question_ai_title_dismissTitle),
        content = {
            WeQuizTextField(
                label = stringResource(R.string.txt_create_question_ai_title_label),
                text = category,
                onTextChanged = { category = it },
                placeholder = stringResource(R.string.txt_create_question_ai_title_placeholder),
            )
        },
        onConfirm = {
            onConfirmButtonClick(category)
        },
        onDismissRequest = onDismissButtonClick,
        confirmButtonEnabled = category.isNotEmpty(),
    )
}

@Preview(locale = "ko")
@PreviewLightDark
@Composable
private fun CreateGroupScreenPreview() {
    WeQuizTheme {
        QuizAiDialog(onConfirmButtonClick = { }, onDismissButtonClick = {})
    }
}
