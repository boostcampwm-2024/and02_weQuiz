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
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuizTitleTextField(
    quizTitle: String,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = quizTitle,
        onValueChange = { onValueChange(it) },
        label = {
            Text(text = "제목")
        },
        placeholder = {
            Text(text = "퀴즈 제목을 입력하세요.")
        },
        maxLines = 1,
        trailingIcon = {
            IconButton(onClick = onClearClick) {
                Icon(
                    imageVector = Icons.Outlined.Cancel,
                    contentDescription = null
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun QuizTitleTextFieldPreview() {
    QuizTitleTextField(
        quizTitle = "",
        onValueChange = {},
        onClearClick = {},
    )
}