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
import kr.boostcamp_2024.course.quiz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionTopBar(
    onShowDialog: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(R.string.top_app_bar_question_title)) },
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
