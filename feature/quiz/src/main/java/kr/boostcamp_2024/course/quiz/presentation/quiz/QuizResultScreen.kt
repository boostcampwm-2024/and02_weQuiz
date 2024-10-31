package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuizResultScreen(
    onNavigationButtonClick: () -> Unit,
    onQuestionClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onNavigationButtonClick
        ) {
            Text(text = "뒤로가기")
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "퀴즈 결과 화면")

            Button(onClick = onQuestionClick) {
                Text(text = "문제")
            }
            Button(onClick = onQuestionClick) {
                Text(text = "문제")
            }
            Button(onClick = onQuestionClick) {
                Text(text = "문제")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultScreenPreview() {
    QuizResultScreen(
        onNavigationButtonClick = {},
        onQuestionClick = {},
    )
}