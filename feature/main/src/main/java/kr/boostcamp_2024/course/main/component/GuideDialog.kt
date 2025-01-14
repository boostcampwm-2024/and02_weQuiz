package kr.boostcamp_2024.course.main.component

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.designsystem.ui.theme.component.WeQuizBaseDialog
import kr.boostcamp_2024.course.main.R

@Composable
fun GuideDialog(
    guideUrl: String,
    onDismissButtonClick: () -> Unit,
) {
    val context = LocalContext.current
    WeQuizBaseDialog(
        title = stringResource(R.string.txt_guide_dialog_content),
        dialogImage = painterResource(id = R.drawable.sample_profile1),
        confirmTitle = stringResource(R.string.txt_guide_dialog_confirm),
        dismissTitle = stringResource(R.string.txt_guide_dialog_cancel),
        content = {},
        onConfirm = {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(guideUrl)))
            onDismissButtonClick()
        },
        onDismissRequest = {
            onDismissButtonClick()
        },
    )
}

@Preview(showBackground = true, locale = "ko")
@Composable
private fun CreateGroupScreenPreview() {
    WeQuizTheme {
        GuideDialog(
            guideUrl = "",
            onDismissButtonClick = {},
        )
    }
}
