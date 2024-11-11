package kr.boostcamp_2024.course.quiz.presentation.quiz

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuizScreen(
    onNavigationButtonClick: () -> Unit,
    onQuizStartButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Button(
            modifier = Modifier.align(Alignment.TopStart),
            onClick = onNavigationButtonClick,
        ) {
            Text(text = "뒤로가기")
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "퀴즈 시작 화면",
        )

        Button(
            modifier = Modifier.align(Alignment.BottomCenter),
            onClick = onQuizStartButtonClick,
        ) {
            Text(text = "퀴즈 시작")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizScreen(
        onNavigationButtonClick = {},
        onQuizStartButtonClick = {},
    )
}
