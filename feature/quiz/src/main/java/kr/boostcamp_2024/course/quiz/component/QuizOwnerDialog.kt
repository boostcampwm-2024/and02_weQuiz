package kr.boostcamp_2024.course.quiz.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_NORMAL
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.quiz.R

@Composable
internal fun QuizOwnerDialog(
    isQuit: Boolean = false,
    onDismissButtonClick: () -> Unit,
    onFinishQuizButtonClick: () -> Unit,
) {
    if (isQuit) {
        WeQuizBaseDialog(
            title = stringResource(R.string.dialog_txt_owner_quit_quiz),
            confirmTitle = stringResource(R.string.txt_finish_quiz),
            dismissTitle = stringResource(R.string.txt_finish_cancel),
            onConfirm = onFinishQuizButtonClick,
            onDismissRequest = onDismissButtonClick,
            dialogImage = painterResource(id = R.drawable.quiz_system_profile),
            content = { /* no-op */ },
        )
    } else {
        WeQuizBaseDialog(
            title = stringResource(R.string.dialog_txt_finish_quiz),
            confirmTitle = stringResource(R.string.txt_quiz_finish_confirm),
            onConfirm = onFinishQuizButtonClick,
            onDismissRequest = onDismissButtonClick,
            dismissButton = null,
            dialogImage = painterResource(id = R.drawable.quiz_system_profile),
            content = { /* no-op */ },
        )
    }
}

@PreviewKoLightDark
@Composable
fun QuizOwnerDialogPreview() {
    WeQuizTheme {
        QuizOwnerDialog(
            isQuit = true,
            onDismissButtonClick = {},
            onFinishQuizButtonClick = {},
        )
    }
}
