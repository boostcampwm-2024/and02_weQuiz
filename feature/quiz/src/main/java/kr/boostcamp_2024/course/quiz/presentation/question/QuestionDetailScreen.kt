package kr.boostcamp_2024.course.quiz.presentation.question

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun QuestionDetailScreen(
    onNavigationButtonClick: () -> Unit,
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

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "문제 상세 화면"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuestionDetailScreenPreview() {
        QuestionDetailScreen(
            onNavigationButtonClick = {},
        )
}