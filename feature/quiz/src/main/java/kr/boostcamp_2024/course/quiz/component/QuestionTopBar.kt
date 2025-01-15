package kr.boostcamp_2024.course.quiz.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kr.boostcamp_2024.course.designsystem.ui.annotation.PreviewKoLightDark
import kr.boostcamp_2024.course.designsystem.ui.theme.WeQuizTheme
import kr.boostcamp_2024.course.quiz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun QuestionTopBar(
    title: String,
    onShowDialog: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = onShowDialog) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.des_btn_back_question),
                )
            }
        },
    )
}

@PreviewKoLightDark
@Composable
private fun QuestionTopBarPreview() {
    WeQuizTheme {
        QuestionTopBar(
            title = "test title",
            onShowDialog = {},
        )
    }
}
