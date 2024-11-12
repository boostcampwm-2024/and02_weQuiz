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
fun QuizDescriptionTextField(
    quizDescription: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = quizDescription,
        onValueChange = { onValueChange(it) },
        label = {
            Text(text = stringResource(R.string.txt_quiz_description_label))
        },
        placeholder = {
            Text(text = stringResource(R.string.txt_quiz_description_placeholder))
        },
        minLines = 6,
        maxLines = 6,
        trailingIcon = {
            IconButton(onClick = { onValueChange("") }) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = null,
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun QuizDescriptionTextFieldPreview() {
    QuizDescriptionTextField(
        quizDescription = "",
        onValueChange = {},
    )
}
