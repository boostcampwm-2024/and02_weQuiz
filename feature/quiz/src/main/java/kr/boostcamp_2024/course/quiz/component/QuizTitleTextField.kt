package kr.boostcamp_2024.course.quiz.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kr.boostcamp_2024.course.quiz.R

@Composable
fun QuizTitleTextField(
    quizTitle: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = quizTitle,
        onValueChange = { onValueChange(it) },
        label = {
            Text(text = stringResource(R.string.txt_quiz_title_label))
        },
        placeholder = {
            Text(text = stringResource(R.string.txt_quiz_title_placeholder))
        },
        maxLines = 1,
        trailingIcon = {
            IconButton(onClick = { onValueChange("") }) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = stringResource(R.string.btn_clear_text),
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun QuizTitleTextFieldPreview() {
    QuizTitleTextField(
        quizTitle = "",
        onValueChange = {},
    )
}
