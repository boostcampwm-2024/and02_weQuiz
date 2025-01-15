package kr.boostcamp_2024.course.study.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizTextField
import kr.boostcamp_2024.course.study.R

@Composable
fun CreateGroupDialog(
    onDismissButtonClick: () -> Unit,
    onConfirmButtonClick: (String, String) -> Unit,
    groupId: String,
    email: String,
    onEmailChanged: (String) -> Unit,
    isEmailValid: Boolean,
    resetEmail: () -> Unit,
) {
    WeQuizBaseDialog(
        title = stringResource(R.string.dialog_create_group_title),
        dialogImage = painterResource(id = R.drawable.member_invite_character),
        confirmTitle = stringResource(R.string.btn_dialog_create_group_confirm),
        dismissTitle = stringResource(R.string.btn_dialog_create_group_dismiss),
        onConfirm = {
            onConfirmButtonClick(groupId, email)
            resetEmail()
        },
        onDismissRequest = onDismissButtonClick,
        content = {
            WeQuizTextField(
                label = stringResource(R.string.textfield_label_create_group),
                text = email,
                onTextChanged = onEmailChanged,
                placeholder = stringResource(R.string.textfield_placeholder_create_group),
            )
        },
        confirmButtonEnabled = isEmailValid,
    )
}

@Preview(showBackground = true, locale = "ko")
@Composable
private fun CreateGroupScreenPreview() {
    WeQuizTheme {
        CreateGroupDialog(
            onConfirmButtonClick = { _, _ -> },
            onDismissButtonClick = {},
            groupId = "id",
            email = "",
            onEmailChanged = {},
            isEmailValid = true,
            resetEmail = {},
        )
    }
}
