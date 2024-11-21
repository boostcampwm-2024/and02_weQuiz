package kr.boostcamp_2024.course.quiz.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.quiz.R

@Composable
fun OwnerFinishQuizDialog(
    onDismissButtonClick: () -> Unit,
    onFinishQuizButtonClick: () -> Unit,
) {
    WeQuizBaseDialog(
        title = stringResource(R.string.dialog_owner_finish_quiz),
        confirmTitle = stringResource(R.string.txt_finish_quiz),
        dismissTitle = stringResource(R.string.txt_finish_cancel),
        onConfirm = onFinishQuizButtonClick,
        onDismissRequest = onDismissButtonClick,
        dialogImage = painterResource(id = R.drawable.quiz_system_profile),
        content = { /* no-op */ },
    )
}

@Preview
@Composable
fun OwnerFinishQuizDialogPreview() {
    WeQuizTheme {
        OwnerFinishQuizDialog(
            onDismissButtonClick = { /* no-op */ },
            onFinishQuizButtonClick = { /* no-op */ },
        )
    }
}
