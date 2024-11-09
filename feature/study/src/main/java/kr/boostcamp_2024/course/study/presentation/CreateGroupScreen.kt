package kr.boostcamp_2024.course.study.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.study.R

@Composable
fun CreateGroupScreen(onDismissButtonClick: () -> Unit, onConfirmButtonClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    WeQuizBaseDialog(
        title = stringResource(R.string.dialog_create_group_title),
        dialogImage = painterResource(id = R.drawable.waterfall),
        confirmTitle = stringResource(R.string.btn_dialog_create_group_confirm),
        dismissTitle = stringResource(R.string.btn_dialog_create_group_dismiss),
        onConfirm = onConfirmButtonClick,
        onDismissRequest = onDismissButtonClick,
        content = {
            WeQuizTextField(
                label = stringResource(R.string.textfield_label_create_group),
                text = email,
                onTextChanged = { email = it },
                placeholder = stringResource(R.string.textfield_placeholder_create_group)
            )
        },
    )
}

@Preview
@Composable
private fun CreateGroupScreenPreview() {
    CreateGroupScreen(onConfirmButtonClick = {}, onDismissButtonClick = {})
}
